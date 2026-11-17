package com.saiful.tmdbexplorer.discover

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.shared.data.repository.DiscoverRepository
import com.saiful.shared.model.WatchProviderItem
import com.saiful.shared.model.account.AccountMediaTab
import com.saiful.shared.model.discover.DiscoverSortBy
import com.saiful.shared.model.discover.GenreItem
import com.saiful.shared.model.discover.GenresResponse
import com.saiful.shared.model.discover.WatchProvidersListResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
internal class DiscoverViewModelTest : BaseViewModelTest() {

    @get:Rule
    internal var coroutineRule = MainCoroutineRule()

    private val discoverRepository: DiscoverRepository = mock()
    private lateinit var viewModel: DiscoverViewModel

    private val actionGenre = GenreItem(id = 28, name = "Action")
    private val dramaGenre = GenreItem(id = 18, name = "Drama")
    private val netflixProvider = WatchProviderItem(providerId = 8, providerName = "Netflix")
    private val disneyProvider = WatchProviderItem(providerId = 337, providerName = "Disney+")

    override fun setup() {
        // Default empty repo responses
    }

    override fun tearDown() {
        if (::viewModel.isInitialized) {
            viewModel.viewModelScope.coroutineContext.cancelChildren()
        }
        reset(discoverRepository)
    }

    private fun initViewModel(savedStateHandle: SavedStateHandle = SavedStateHandle()) {
        viewModel = DiscoverViewModel(
            discoverRepository = discoverRepository,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `default initial filter state is initialized with Movies and POPULARITY_DESC`() = runTest {
        initViewModel()

        val filter = viewModel.filter.value
        assertEquals(AccountMediaTab.MOVIES, filter.mediaTab)
        assertNull(filter.genre)
        assertNull(filter.year)
        assertNull(filter.provider)
        assertEquals(DiscoverSortBy.POPULARITY_DESC, filter.sortBy)
        assertEquals(0, filter.activeFilterCount)
    }

    @Test
    fun `nav arguments initialize filter state with provided values`() = runTest {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "mediaType" to "tv",
                "genreId" to 18,
                "genreName" to "Drama",
                "year" to 2024,
                "providerId" to 8,
                "providerName" to "Netflix"
            )
        )
        initViewModel(savedStateHandle)

        val filter = viewModel.filter.value
        assertEquals(AccountMediaTab.TV_SHOWS, filter.mediaTab)
        assertEquals(dramaGenre, filter.genre)
        assertEquals(2024, filter.year)
        assertEquals(8, filter.provider?.providerId)
        assertEquals("Netflix", filter.provider?.providerName)
        assertEquals(3, filter.activeFilterCount)
    }

    @Test
    fun `loadFilterMetadata updates genres and watch providers from repository`() = runTest {
        whenever(discoverRepository.getMovieGenres()).thenReturn(
            BaseResponse.Success(GenresResponse(listOf(actionGenre)))
        )
        whenever(discoverRepository.getTvGenres()).thenReturn(
            BaseResponse.Success(GenresResponse(listOf(dramaGenre)))
        )
        whenever(discoverRepository.getMovieWatchProviders("US")).thenReturn(
            BaseResponse.Success(WatchProvidersListResponse(listOf(netflixProvider)))
        )

        initViewModel()
        advanceUntilIdle()

        assertEquals(listOf(actionGenre), viewModel.movieGenres.value)
        assertEquals(listOf(dramaGenre), viewModel.tvGenres.value)
        assertEquals(listOf(netflixProvider), viewModel.watchProviders.value)
    }

    @Test
    fun `selectMediaTab switches tab and clears selected genre`() = runTest {
        initViewModel()
        viewModel.selectGenre(actionGenre)
        assertEquals(actionGenre, viewModel.filter.value.genre)

        viewModel.selectMediaTab(AccountMediaTab.TV_SHOWS)

        assertEquals(AccountMediaTab.TV_SHOWS, viewModel.filter.value.mediaTab)
        assertNull(viewModel.filter.value.genre)
    }

    @Test
    fun `selectGenre toggles genre selection`() = runTest {
        initViewModel()

        viewModel.selectGenre(actionGenre)
        assertEquals(actionGenre, viewModel.filter.value.genre)

        // Selecting same genre toggles it off
        viewModel.selectGenre(actionGenre)
        assertNull(viewModel.filter.value.genre)
    }

    @Test
    fun `selectYear toggles year selection`() = runTest {
        initViewModel()

        viewModel.selectYear(2024)
        assertEquals(2024, viewModel.filter.value.year)

        // Selecting same year toggles it off
        viewModel.selectYear(2024)
        assertNull(viewModel.filter.value.year)
    }

    @Test
    fun `selectProvider toggles provider selection`() = runTest {
        initViewModel()

        viewModel.selectProvider(netflixProvider)
        assertEquals(netflixProvider, viewModel.filter.value.provider)

        // Selecting same provider toggles it off
        viewModel.selectProvider(netflixProvider)
        assertNull(viewModel.filter.value.provider)
    }

    @Test
    fun `selectSortBy updates sort order`() = runTest {
        initViewModel()

        viewModel.selectSortBy(DiscoverSortBy.VOTE_AVERAGE_DESC)
        assertEquals(DiscoverSortBy.VOTE_AVERAGE_DESC, viewModel.filter.value.sortBy)
    }

    @Test
    fun `resetFilters restores default filter while preserving media tab`() = runTest {
        initViewModel()
        viewModel.selectMediaTab(AccountMediaTab.TV_SHOWS)
        viewModel.selectGenre(dramaGenre)
        viewModel.selectYear(2022)
        viewModel.selectProvider(disneyProvider)
        viewModel.selectSortBy(DiscoverSortBy.REVENUE_DESC)

        viewModel.resetFilters()

        val filter = viewModel.filter.value
        assertEquals(AccountMediaTab.TV_SHOWS, filter.mediaTab)
        assertNull(filter.genre)
        assertNull(filter.year)
        assertNull(filter.provider)
        assertEquals(DiscoverSortBy.POPULARITY_DESC, filter.sortBy)
        assertEquals(0, filter.activeFilterCount)
    }
}
