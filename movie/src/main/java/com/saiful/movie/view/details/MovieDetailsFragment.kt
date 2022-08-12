package com.saiful.movie.view.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.saiful.base.util.AppConstants.backdropSize
import com.saiful.base.util.AppConstants.imageBaseUrl
import com.saiful.base.util.AppConstants.posterSize
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.R
import com.saiful.movie.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment<FragmentMovieDetailsBinding>() {

    private val args : MovieDetailsFragmentArgs by navArgs()
    private val viewModel : MovieDetailsVM by viewModels()

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

        bindingView.collapsingAppbar.setExpandedTitleColor(Color.TRANSPARENT)
        lifecycleScope.launchWhenStarted {
            viewModel.movieDetails.collect {
                Glide.with(requireContext())
                    .load(imageBaseUrl + backdropSize + it?.backdropPath)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .error(R.drawable.image1)
                    .into(bindingView.backdropImage)

                Glide.with(requireContext())
                    .load(imageBaseUrl + posterSize + it?.posterPath)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .error(R.drawable.image1)
                    .into(bindingView.posterImage)

                bindingView.toolbar.title = it?.title

            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        bindingView.toolbar.setupWithNavController(navController, appBarConfiguration)

    }
}

