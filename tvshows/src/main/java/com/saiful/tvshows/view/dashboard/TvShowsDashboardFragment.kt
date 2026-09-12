package com.saiful.tvshows.view.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.shared.utils.navigateSafe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowsDashboardFragment : Fragment() {

    private val viewModel: ShowsDashboardVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TMDBTheme {
                    TvShowsDashboardScreen(
                        viewModel = viewModel,
                        onShowClick = { showId ->
                            val action = TvShowsDashboardFragmentDirections.actionTvShowsFragmentToTvShowsDetailsFragment(showId)
                            findNavController().navigateSafe(action)
                        },
                        onSeeAllClick = { category ->
                            val action = TvShowsDashboardFragmentDirections.actionTvShowsFragmentToShowListFragment(category)
                            findNavController().navigateSafe(action)
                        }
                    )
                }
            }
        }
    }
}
