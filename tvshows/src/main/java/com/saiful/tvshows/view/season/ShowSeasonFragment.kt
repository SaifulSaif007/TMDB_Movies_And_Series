package com.saiful.tvshows.view.season

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
import com.saiful.base.util.ItemDecorator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.shared.utils.loadBackDropSizeImage
import com.saiful.tvshows.R
import com.saiful.tvshows.databinding.FragmentSeasonsDetailsBinding
import com.saiful.tvshows.view.adapter.EpisodeListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ShowSeasonFragment : BaseFragment<FragmentSeasonsDetailsBinding>() {

    private val viewModel: ShowSeasonVM by viewModels()
    private val args: ShowSeasonFragmentArgs by navArgs()
    private val episodeAdapter = EpisodeListAdapter()

    @Inject
    lateinit var itemDecorator: ItemDecorator

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSeasonsDetailsBinding {
        return FragmentSeasonsDetailsBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_seasons_details

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {

        viewModel.fetchSeason(showId = args.showId, seasonNo = args.seasonNo)

        bindingView.episodeRecycler.apply {
            adapter = episodeAdapter
            addItemDecoration(itemDecorator)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.seasonDetails.collect { season ->
                bindingView.apply {
                    backdropImage.loadBackDropSizeImage(season?.posterPath)
                    seasonName.text = season?.name
                    toolbar.title = season?.name
                    season?.overview.takeIf { !it.isNullOrEmpty() }?.let {
                        seasonOverview.visibility = View.VISIBLE
                        seasonOverview.text = it
                    }

                    season?.episodes?.let { episodeAdapter.submitList(it) }
                }
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

}