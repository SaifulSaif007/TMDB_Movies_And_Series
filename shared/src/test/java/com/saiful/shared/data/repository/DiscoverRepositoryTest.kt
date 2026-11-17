package com.saiful.shared.data.repository

import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.shared.data.api.DiscoverApiService
import com.saiful.shared.model.Movies
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.WatchProviderItem
import com.saiful.shared.model.account.AccountMediaTab
import com.saiful.shared.model.discover.DiscoverFilter
import com.saiful.shared.model.discover.DiscoverMoviesResponse
import com.saiful.shared.model.discover.DiscoverSortBy
import com.saiful.shared.model.discover.DiscoverTvShowsResponse
import com.saiful.shared.model.discover.GenreItem
import com.saiful.shared.model.discover.GenresResponse
import com.saiful.shared.model.discover.WatchProvidersListResponse
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DiscoverRepositoryTest : BaseRepositoryTest() {

    private val discoverApiService: DiscoverApiService = mock()
    private lateinit var discoverRepository: DiscoverRepository

    private val movieItem = Movies(id = 1, title = "Inception", voteAverage = 8.8)
    private val tvItem = TvShows(id = 2, name = "Breaking Bad", voteAverage = 9.5)
    private val genreItem = GenreItem(id = 28, name = "Action")
    private val providerItem = WatchProviderItem(providerId = 8, providerName = "Netflix")

    @Before
    override fun setup() {
        discoverRepository = DiscoverRepository(discoverApiService)
    }

    @After
    override fun tearDown() {
        reset(discoverApiService)
    }

    @Test
    fun `discoverMovies calls api with correct mapped parameters`() {
        runBlocking {
            val filter = DiscoverFilter(
                mediaTab = AccountMediaTab.MOVIES,
                genre = genreItem,
                year = 2024,
                provider = providerItem,
                sortBy = DiscoverSortBy.POPULARITY_DESC
            )
            val expectedResponse = DiscoverMoviesResponse(
                page = 1,
                results = listOf(movieItem),
                totalPages = 5,
                totalResults = 100
            )
            whenever(
                discoverApiService.discoverMovies(
                    page = 1,
                    sortBy = "popularity.desc",
                    withGenres = "28",
                    primaryReleaseYear = 2024,
                    withWatchProviders = "8",
                    watchRegion = "US",
                    voteCountGte = null
                )
            ).thenReturn(BaseResponse.Success(expectedResponse))

            val result = discoverRepository.discoverMovies(1, filter)

            assertTrue(result is BaseResponse.Success)
            assertEquals(expectedResponse, (result as BaseResponse.Success).body)
            verify(discoverApiService).discoverMovies(
                page = 1,
                sortBy = "popularity.desc",
                withGenres = "28",
                primaryReleaseYear = 2024,
                withWatchProviders = "8",
                watchRegion = "US",
                voteCountGte = null
            )
        }
    }

    @Test
    fun `discoverMovies sets voteCountGte threshold when sorting by vote average`() {
        runBlocking {
            val filter = DiscoverFilter(
                sortBy = DiscoverSortBy.VOTE_AVERAGE_DESC
            )
            val expectedResponse = DiscoverMoviesResponse(page = 1)
            whenever(
                discoverApiService.discoverMovies(
                    page = 1,
                    sortBy = "vote_average.desc",
                    withGenres = null,
                    primaryReleaseYear = null,
                    withWatchProviders = null,
                    watchRegion = "US",
                    voteCountGte = 100
                )
            ).thenReturn(BaseResponse.Success(expectedResponse))

            val result = discoverRepository.discoverMovies(1, filter)

            assertTrue(result is BaseResponse.Success)
            verify(discoverApiService).discoverMovies(
                page = 1,
                sortBy = "vote_average.desc",
                withGenres = null,
                primaryReleaseYear = null,
                withWatchProviders = null,
                watchRegion = "US",
                voteCountGte = 100
            )
        }
    }

    @Test
    fun `discoverTvShows calls api with correct mapped parameters`() {
        runBlocking {
            val filter = DiscoverFilter(
                mediaTab = AccountMediaTab.TV_SHOWS,
                genre = genreItem,
                year = 2023,
                provider = providerItem,
                sortBy = DiscoverSortBy.VOTE_AVERAGE_DESC
            )
            val expectedResponse = DiscoverTvShowsResponse(
                page = 1,
                results = listOf(tvItem),
                totalPages = 2,
                totalResults = 40
            )
            whenever(
                discoverApiService.discoverTvShows(
                    page = 1,
                    sortBy = "vote_average.desc",
                    withGenres = "28",
                    firstAirDateYear = 2023,
                    withWatchProviders = "8",
                    watchRegion = "US",
                    voteCountGte = 50
                )
            ).thenReturn(BaseResponse.Success(expectedResponse))

            val result = discoverRepository.discoverTvShows(1, filter)

            assertTrue(result is BaseResponse.Success)
            assertEquals(expectedResponse, (result as BaseResponse.Success).body)
            verify(discoverApiService).discoverTvShows(
                page = 1,
                sortBy = "vote_average.desc",
                withGenres = "28",
                firstAirDateYear = 2023,
                withWatchProviders = "8",
                watchRegion = "US",
                voteCountGte = 50
            )
        }
    }

    @Test
    fun `getMovieGenres returns genres from api`() {
        runBlocking {
            val genresResponse = GenresResponse(genres = listOf(genreItem))
            whenever(discoverApiService.getMovieGenres()).thenReturn(BaseResponse.Success(genresResponse))

            val result = discoverRepository.getMovieGenres()

            assertTrue(result is BaseResponse.Success)
            assertEquals(listOf(genreItem), (result as BaseResponse.Success).body.genres)
        }
    }

    @Test
    fun `getTvGenres returns genres from api`() {
        runBlocking {
            val genresResponse = GenresResponse(genres = listOf(genreItem))
            whenever(discoverApiService.getTvGenres()).thenReturn(BaseResponse.Success(genresResponse))

            val result = discoverRepository.getTvGenres()

            assertTrue(result is BaseResponse.Success)
            assertEquals(listOf(genreItem), (result as BaseResponse.Success).body.genres)
        }
    }

    @Test
    fun `getMovieWatchProviders returns providers from api`() {
        runBlocking {
            val providersResponse = WatchProvidersListResponse(results = listOf(providerItem))
            whenever(discoverApiService.getMovieWatchProviders("US")).thenReturn(BaseResponse.Success(providersResponse))

            val result = discoverRepository.getMovieWatchProviders("US")

            assertTrue(result is BaseResponse.Success)
            assertEquals(listOf(providerItem), (result as BaseResponse.Success).body.results)
        }
    }
}
