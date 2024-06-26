package com.saiful.movie.view.details

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.chip.Chip
import com.saiful.base.util.ItemDecorator
import com.saiful.base.util.navigation.PersonModuleNavigation
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.R
import com.saiful.movie.databinding.FragmentMovieDetailsBinding
import com.saiful.movie.model.GenresItem
import com.saiful.movie.view.adapter.*
import com.saiful.shared.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment<FragmentMovieDetailsBinding>() {

    private val args: MovieDetailsFragmentArgs by navArgs()
    private val viewModel: MovieDetailsVM by viewModels()
    private val trailerAdapter = MovieTrailerAdapter(::onTrailerClick)
    private val castAdapter = MovieCastAdapter(::castItemClick)
    private val recommendationAdapter = MovieDashboardAdapter(::movieItemClick)
    private val similarAdapter = MovieDashboardAdapter(::movieItemClick)

    @Inject
    lateinit var itemDecorator: ItemDecorator

    @Inject
    lateinit var personNavigation: PersonModuleNavigation

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDetailsBinding {
        return FragmentMovieDetailsBinding.inflate(inflater, container, false)
    }

    override fun layoutId() = R.layout.fragment_movie_details

    override fun getViewModel(): BaseViewModel = viewModel

    @SuppressLint("SetTextI18n")
    override fun initOnCreateView() {
        viewModel.fetchMovieDetails(args.movieId)

        bindingView.apply {
            movieCastLayout.apply {
                castRecycler.apply {
                    adapter = castAdapter
                    addItemDecoration(itemDecorator)
                }
            }
            movieTrailerLayout.apply {
                trailerRecycler.apply {
                    adapter = trailerAdapter
                    addItemDecoration(itemDecorator)
                }
            }
            movieRecommendationLayout.apply {
                recommendationMovieRecycler.apply {
                    adapter = recommendationAdapter
                    addItemDecoration(itemDecorator)
                }
            }
            similarMovieLayout.apply {
                similarMovieRecycler.apply {
                    adapter = similarAdapter
                    addItemDecoration(itemDecorator)
                }
            }

            movieCollectionLayout.root.setOnClickListener {
                val collectionId =
                    viewModel.movieDetailsResponse.value?.belongsToCollection?.id ?: 0
                findNavController().navigate(
                    R.id.movie_collection_nav_graph,
                    bundleOf("collection_id" to collectionId)
                )
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.movieDetailsResponse.collect { movie ->
                bindingView.apply {
                    backdropImage.loadBackDropSizeImage(movie?.backdropPath)
                    posterImage.loadPosterSizeImage(movie?.posterPath)
                    toolbar.title = movie?.title
                    movieName.text = movie?.title
                    ratingBar2.rating = movie?.voteAverage?.toFloat() ?: 0f
                    movieRating.text =
                        "(" + floatNumberFormatter(movie?.voteAverage?.toFloat()) + ")"

                    addChips(movie?.genres)

                    movieTagline.text = movie?.tagline
                    movieOverview.text = movie?.overview

                    movieInfoLayout.apply {
                        movieBudget.text = movie?.budget?.toLong()?.formatToShortNumber()
                        movieRevenue.text = movie?.revenue?.toLong()?.formatToShortNumber()
                        movieStatus.text = movie?.status
                        movieReleaseDate.text = movie?.releaseDate?.formatDate()
                        movieRuntime.text = movie?.runtime.toString().plus(" mins")
                        movieProduction.text =
                            movie?.productionCompanies?.map { it?.name }?.joinToString(", ")
                    }

                    val movieTrailers =
                        movie?.videos?.results?.filter { it.type == "Trailer" || it.type == "Teaser" }

                    movieTrailers?.let {
                        trailerAdapter.submitList(it)
                    }

                    if (movie?.belongsToCollection != null) {
                        movieCollectionLayout.apply {
                            collectionImage.loadPosterSizeImage(movie.belongsToCollection.posterPath)

                            collectionName.text = movie.belongsToCollection.name
                            movieCollectionLayout.root.visibility = View.VISIBLE
                        }
                    } else {
                        movieCollectionLayout.root.visibility = View.GONE
                    }

                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.movieCast.collect {
                it?.cast?.let { casts -> castAdapter.submitList(casts) }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.recommendation.collect {
                it?.results?.let { recommend -> recommendationAdapter.submitList(recommend) }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.similar.collect {
                it?.results?.let { similar -> similarAdapter.submitList(similar) }
            }
        }
    }

    private fun addChips(genres: List<GenresItem?>?) {
        if (genres != null) {
            for (chip in genres) {
                bindingView.chipGroup.addView(
                    createTagChip(
                        requireContext(),
                        chip?.name.toString()
                    )
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingView.collapsingAppbar.setExpandedTitleColor(Color.TRANSPARENT)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        bindingView.toolbar.setupWithNavController(navController, appBarConfiguration)

    }

    private fun onTrailerClick(key: String) {
        try {
            val youtubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://$key"))
            youtubeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(youtubeIntent)
        } catch (e: ActivityNotFoundException) {
            val otherIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=$key"))
            startActivity(otherIntent)
        }
    }

    private fun createTagChip(context: Context, chipName: String): Chip {
        return Chip(context).apply {
            text = chipName
            textStartPadding = 0f
            textStartPadding = 0f
        }

    }

    private fun movieItemClick(movieId: Int) {
        findNavController().navigateSafe(
            R.id.action_movieDetailsFragment_self,
            bundleOf("movie_id" to movieId)
        )
    }

    private fun castItemClick(personId: Int) {
        personNavigation.navigateToPersonDetails(personId, findNavController())
    }
}

