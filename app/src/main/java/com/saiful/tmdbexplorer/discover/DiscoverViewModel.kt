package com.saiful.tmdbexplorer.discover

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.saiful.base.network.model.BaseResponse
import com.saiful.shared.data.repository.DiscoverRepository
import com.saiful.shared.data.repository.paging.DiscoverMoviesPagingSource
import com.saiful.shared.data.repository.paging.DiscoverTvShowsPagingSource
import com.saiful.shared.model.Movies
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.WatchProviderItem
import com.saiful.shared.model.account.AccountMediaTab
import com.saiful.shared.model.discover.DiscoverFilter
import com.saiful.shared.model.discover.DiscoverSortBy
import com.saiful.shared.model.discover.GenreItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverRepository: DiscoverRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navMediaType: String? = savedStateHandle["mediaType"]
    private val navGenreId: Int? = savedStateHandle["genreId"]
    private val navGenreName: String? = savedStateHandle["genreName"]
    private val navYear: Int? = savedStateHandle["year"]
    private val navProviderId: Int? = savedStateHandle["providerId"]
    private val navProviderName: String? = savedStateHandle["providerName"]

    private val initialMediaTab =
        if (navMediaType?.equals("tv", ignoreCase = true) == true) AccountMediaTab.TV_SHOWS else AccountMediaTab.MOVIES
    private val initialGenre =
        if (navGenreId != null && navGenreName != null) GenreItem(navGenreId, navGenreName) else null
    private val initialProvider =
        if (navProviderId != null && navProviderName != null) {
            WatchProviderItem(providerId = navProviderId, providerName = navProviderName)
        } else null

    private val _filter = MutableStateFlow(
        DiscoverFilter(
            mediaTab = initialMediaTab,
            genre = initialGenre,
            year = navYear,
            provider = initialProvider,
            sortBy = DiscoverSortBy.POPULARITY_DESC
        )
    )
    val filter: StateFlow<DiscoverFilter> = _filter.asStateFlow()

    private val _movieGenres = MutableStateFlow<List<GenreItem>>(emptyList())
    val movieGenres: StateFlow<List<GenreItem>> = _movieGenres.asStateFlow()

    private val _tvGenres = MutableStateFlow<List<GenreItem>>(emptyList())
    val tvGenres: StateFlow<List<GenreItem>> = _tvGenres.asStateFlow()

    private val _watchProviders = MutableStateFlow<List<WatchProviderItem>>(emptyList())
    val watchProviders: StateFlow<List<WatchProviderItem>> = _watchProviders.asStateFlow()

    val availableYears: List<Int> = listOf(
        2026, 2025, 2024, 2023, 2022, 2021, 2020, 2019, 2018, 2015, 2010, 2000, 1990
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val discoverMovies: Flow<PagingData<Movies>> = _filter.flatMapLatest { currentFilter ->
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false)
        ) {
            DiscoverMoviesPagingSource(discoverRepository, currentFilter)
        }.flow.cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val discoverTvShows: Flow<PagingData<TvShows>> = _filter.flatMapLatest { currentFilter ->
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false)
        ) {
            DiscoverTvShowsPagingSource(discoverRepository, currentFilter)
        }.flow.cachedIn(viewModelScope)
    }

    init {
        loadFilterMetadata()
    }

    private fun loadFilterMetadata() {
        viewModelScope.launch {
            when (val response = discoverRepository.getMovieGenres()) {
                is BaseResponse.Success -> _movieGenres.value = response.body.genres
                else -> _movieGenres.value = defaultMovieGenres
            }
        }

        viewModelScope.launch {
            when (val response = discoverRepository.getTvGenres()) {
                is BaseResponse.Success -> _tvGenres.value = response.body.genres
                else -> _tvGenres.value = defaultTvGenres
            }
        }

        viewModelScope.launch {
            when (val response = discoverRepository.getMovieWatchProviders("US")) {
                is BaseResponse.Success -> {
                    val list = response.body.results.filter { it.providerId != null }
                    if (list.isNotEmpty()) {
                        _watchProviders.value = list.take(15)
                    } else {
                        _watchProviders.value = defaultPopularProviders
                    }
                }
                else -> _watchProviders.value = defaultPopularProviders
            }
        }
    }

    fun selectMediaTab(tab: AccountMediaTab) {
        if (_filter.value.mediaTab != tab) {
            _filter.value = _filter.value.copy(
                mediaTab = tab,
                genre = null // Reset genre when switching between Movies and TV since genre IDs differ
            )
        }
    }

    fun selectGenre(genre: GenreItem?) {
        val newGenre = if (_filter.value.genre?.id == genre?.id) null else genre
        _filter.value = _filter.value.copy(genre = newGenre)
    }

    fun selectYear(year: Int?) {
        val newYear = if (_filter.value.year == year) null else year
        _filter.value = _filter.value.copy(year = newYear)
    }

    fun selectProvider(provider: WatchProviderItem?) {
        val newProvider = if (_filter.value.provider?.providerId == provider?.providerId) null else provider
        _filter.value = _filter.value.copy(provider = newProvider)
    }

    fun selectSortBy(sortBy: DiscoverSortBy) {
        _filter.value = _filter.value.copy(sortBy = sortBy)
    }

    fun resetFilters() {
        _filter.value = DiscoverFilter(
            mediaTab = _filter.value.mediaTab,
            genre = null,
            year = null,
            provider = null,
            sortBy = DiscoverSortBy.POPULARITY_DESC
        )
    }

    companion object {
        val defaultMovieGenres = listOf(
            GenreItem(28, "Action"),
            GenreItem(12, "Adventure"),
            GenreItem(16, "Animation"),
            GenreItem(35, "Comedy"),
            GenreItem(80, "Crime"),
            GenreItem(99, "Documentary"),
            GenreItem(18, "Drama"),
            GenreItem(10751, "Family"),
            GenreItem(14, "Fantasy"),
            GenreItem(27, "Horror"),
            GenreItem(9648, "Mystery"),
            GenreItem(10749, "Romance"),
            GenreItem(878, "Sci-Fi"),
            GenreItem(53, "Thriller")
        )

        val defaultTvGenres = listOf(
            GenreItem(10759, "Action & Adventure"),
            GenreItem(16, "Animation"),
            GenreItem(35, "Comedy"),
            GenreItem(80, "Crime"),
            GenreItem(99, "Documentary"),
            GenreItem(18, "Drama"),
            GenreItem(10762, "Kids"),
            GenreItem(9648, "Mystery"),
            GenreItem(10764, "Reality"),
            GenreItem(10765, "Sci-Fi & Fantasy")
        )

        val defaultPopularProviders = listOf(
            WatchProviderItem(providerId = 8, providerName = "Netflix", logoPath = "/pbpMk2JmcoNnQwx5JGpXngfoWtp.jpg"),
            WatchProviderItem(providerId = 337, providerName = "Disney+", logoPath = "/7rwgEs15tFwyR9NPQ5vpzxTj19Q.jpg"),
            WatchProviderItem(providerId = 9, providerName = "Amazon Prime", logoPath = "/pvsWk59E7Vn5h6mY0b6UeI868L1.jpg"),
            WatchProviderItem(providerId = 350, providerName = "Apple TV+", logoPath = "/6uhKBfmtzFqOcLoulGbZox0Jnu.jpg"),
            WatchProviderItem(providerId = 1899, providerName = "Max", logoPath = "/fksCUZ9QDWZMUwL2LgfhAwVNQi0.jpg"),
            WatchProviderItem(providerId = 15, providerName = "Hulu", logoPath = "/zxrVdFj005jlujCR0wsrvumggfn.jpg"),
            WatchProviderItem(providerId = 531, providerName = "Paramount+", logoPath = "/xbhHLa3ktAVUQJ9UpFiGH3DW5SB.jpg")
        )
    }
}
