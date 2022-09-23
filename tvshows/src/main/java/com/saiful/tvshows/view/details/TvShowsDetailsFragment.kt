package com.saiful.tvshows.view.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.Chip
import com.saiful.base.util.ItemDecorator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.floatNumberFormatter
import com.saiful.shared.utils.formatDate
import com.saiful.shared.utils.navigateSafe
import com.saiful.tvshows.R
import com.saiful.tvshows.databinding.FragmentTvshowDetailsBinding
import com.saiful.tvshows.model.Genre
import com.saiful.tvshows.view.adapter.ShowCastAdapter
import com.saiful.tvshows.view.adapter.ShowSeasonAdapter
import com.saiful.tvshows.view.adapter.ShowsDashboardAdapter
import com.saiful.tvshows.view.adapter.ShowsTrailerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class TvShowsDetailsFragment : BaseFragment<FragmentTvshowDetailsBinding>() {

    private val args: TvShowsDetailsFragmentArgs by navArgs()
    private val viewModel: TvShowsDetailsVM by viewModels()
    private val castAdapter = ShowCastAdapter()
    private val trailerAdapter = ShowsTrailerAdapter(::onTrailerClick)
    private val recommendationAdapter = ShowsDashboardAdapter(::showItemClick)
    private val similarAdapter = ShowsDashboardAdapter(::showItemClick)
    private val seasonAdapter = ShowSeasonAdapter(::seasonItemClick)

    @Inject
    lateinit var itemDecorator: ItemDecorator

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTvshowDetailsBinding {
        return FragmentTvshowDetailsBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_tvshow_details

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {
        viewModel.fetchShowDetails(args.showId)

        bindingView.apply {
            showsCastLayout.apply {
                castRecycler.apply {
                    adapter = castAdapter
                    addItemDecoration(itemDecorator)
                }
            }
            showsTrailerLayout.apply {
                trailerRecycler.apply {
                    adapter = trailerAdapter
                    addItemDecoration(itemDecorator)
                }
            }
            showsRecommendationLayout.apply {
                recommendationMovieRecycler.apply {
                    adapter = recommendationAdapter
                    addItemDecoration(itemDecorator)
                }
            }
            similarShowsLayout.apply {
                similarMovieRecycler.apply {
                    adapter = similarAdapter
                    addItemDecoration(itemDecorator)
                }
            }
            showsSeasonLayout.apply {
                seasonRecycler.apply {
                    adapter = seasonAdapter
                    addItemDecoration(itemDecorator)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.showDetails.collect { shows ->
                Glide.with(requireContext())
                    .load(AppConstants.imageBaseUrl + AppConstants.backdropSize + shows?.backdropPath)
                    .transition(DrawableTransitionOptions.withCrossFade(100))
                    //.error(R.drawable.image1)
                    .into(bindingView.backdropImage)

                Glide.with(requireContext())
                    .load(AppConstants.imageBaseUrl + AppConstants.posterSize + shows?.posterPath)
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    //.error(R.drawable.image1)
                    .into(bindingView.posterImage)

                bindingView.apply {
                    toolbar.title = shows?.name
                    showsName.text = shows?.name
                    ratingBar2.rating = shows?.voteAverage?.toFloat() ?: 0f
                    showsRating.text =
                        "(" + floatNumberFormatter(shows?.voteAverage?.toFloat() ?: 0f) + ")"

                    addChips(shows?.genres)

                    showsTagline.text = shows?.tagline
                    showsOverview.text = shows?.overview

                    showsInfoLayout.apply {
                        createdBy.text = shows?.createdBy?.map { it?.name }?.joinToString(", ")
                        firstAirDate.text = shows?.firstAirDate?.formatDate()
                        showsStatus.text = shows?.status
                        showsLanguage.text =
                            shows?.spokenLanguage?.map { it?.englishName }?.joinToString(", ")
                        showsNetwork.text =
                            shows?.networks?.map { it?.name }?.joinToString(", ")
                        showProduction.text =
                            shows?.productionCompanies?.map { it?.name }?.joinToString(", ")

                    }

                    shows?.videos?.results?.let {
                        trailerAdapter.submitList(it)
                    }

                    shows?.seasons?.filter { it.seasonNumber > 0 }?.let {
                        seasonAdapter.submitList(it)
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.showCasts.collect {
                it?.cast?.let { casts -> castAdapter.submitList(casts) }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.recommendation.collect {
                it?.results?.let { recommendation -> recommendationAdapter.submitList(recommendation) }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.similarShow.collect {
                it?.results?.let { similar -> similarAdapter.submitList(similar) }
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

    private fun addChips(genres: List<Genre?>?) {
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

    private fun createTagChip(context: Context, chipName: String): Chip {
        return Chip(context).apply {
            text = chipName
            textStartPadding = 0f
            textStartPadding = 0f
        }
    }

    private fun showItemClick(showId: Int) {
        findNavController().navigateSafe(
            R.id.action_tvShowsDetailsFragment_self,
            bundleOf("show_id" to showId)
        )
    }

    private fun seasonItemClick(seasonNo: Int){
        findNavController().navigateSafe(TvShowsDetailsFragmentDirections.actionTvShowsDetailsFragmentToShowSeason(
            args.showId, seasonNo
        ))
    }
}