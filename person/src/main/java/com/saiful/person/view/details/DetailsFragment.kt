package com.saiful.person.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.util.navigation.MovieModuleNavigation
import com.saiful.base.util.navigation.TvShowModuleNavigation
import com.saiful.shared.R
import com.saiful.shared.utils.JsonConverter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: PersonDetailsVM by viewModels()

    @Inject
    lateinit var movieModuleNavigation: MovieModuleNavigation

    @Inject
    lateinit var tvShowModuleNavigation: TvShowModuleNavigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TMDBTheme {
                    PersonDetailsScreen(
                        personId = args.personId,
                        viewModel = viewModel,
                        onBackClick = { findNavController().navigateUp() },
                        onMovieClick = { movieId ->
                            movieModuleNavigation.navigateMovieDetails(movieId, findNavController())
                        },
                        onShowClick = { showsId ->
                            tvShowModuleNavigation.navigateToShowDetails(showsId, findNavController())
                        },
                        onImageClick = { position, images ->
                            findNavController().navigate(
                                R.id.image_gallery_nav_graph,
                                bundleOf(
                                    "images" to JsonConverter.toJsonList(images),
                                    "start_index" to position
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
