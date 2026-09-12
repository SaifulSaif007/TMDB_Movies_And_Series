package com.saiful.tvshows.view.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.util.navigation.PersonModuleNavigation
import com.saiful.shared.utils.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TvShowsDetailsFragment : Fragment() {

    private val viewModel: TvShowsDetailsVM by viewModels()
    private val args: TvShowsDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var personNavigation: PersonModuleNavigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TMDBTheme {
                    TvShowsDetailsScreen(
                        showId = args.showId,
                        viewModel = viewModel,
                        onBackClick = { findNavController().navigateUp() },
                        onShowClick = { showId ->
                            val action = TvShowsDetailsFragmentDirections.actionTvShowsDetailsFragmentSelf(showId)
                            findNavController().navigateSafe(action)
                        },
                        onCastClick = { castId ->
                            personNavigation.navigateToPersonDetails(castId, findNavController())
                        },
                        onTrailerClick = { key ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$key"))
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}
