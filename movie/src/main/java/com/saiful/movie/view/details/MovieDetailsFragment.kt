package com.saiful.movie.view.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.Chip
import com.saiful.base.util.AppConstants.backdropSize
import com.saiful.base.util.AppConstants.imageBaseUrl
import com.saiful.base.util.AppConstants.posterSize
import com.saiful.base.util.ItemDecorator
import com.saiful.base.util.floatNumberFormatter
import com.saiful.base.util.formatDate
import com.saiful.base.util.formatToShortNumber
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.R
import com.saiful.movie.databinding.FragmentMovieDetailsBinding
import com.saiful.movie.model.GenresItem
import com.saiful.movie.view.adapter.MovieTrailerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment<FragmentMovieDetailsBinding>() {

    private val args: MovieDetailsFragmentArgs by navArgs()
    private val viewModel: MovieDetailsVM by viewModels()
    private val trailerAdapter = MovieTrailerAdapter(::onTrailerClick)

    @Inject
    lateinit var itemDecorator: ItemDecorator

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDetailsBinding {
        return FragmentMovieDetailsBinding.inflate(inflater, container, false)
    }

    override fun layoutId() = R.layout.fragment_movie_details

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {
        viewModel.fetchMovieDetails(args.movieId)

        lifecycleScope.launchWhenStarted {
            viewModel.movieDetails.collect { movie ->
                Glide.with(requireContext())
                    .load(imageBaseUrl + backdropSize + movie?.backdropPath)
                    .transition(DrawableTransitionOptions.withCrossFade(100))
                    .error(R.drawable.image1)
                    .into(bindingView.backdropImage)

                Glide.with(requireContext())
                    .load(imageBaseUrl + posterSize + movie?.posterPath)
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .error(R.drawable.image1)
                    .into(bindingView.posterImage)

                bindingView.apply {
                    toolbar.title = movie?.title
                    movieName.text = movie?.title
                    ratingBar2.rating = movie?.voteAverage?.toFloat() ?: 0f
                    movieRating.text =
                        "(" + floatNumberFormatter(movie?.voteAverage?.toFloat() ?: 0f) + ")"

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


                    movieTrailerLayout.apply {
                        trailerRecycler.apply {
                            adapter = trailerAdapter
                            addItemDecoration(itemDecorator)
                        }
                        movie?.videos?.results?.let {
                            trailerAdapter.submitList(it)
                        }
                    }
                }
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
}

