package com.saiful.person.view.dashboard

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
import com.saiful.person.R
import com.saiful.shared.utils.navigateSafe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonDashboardFragment : Fragment() {

    private val viewModel: PersonDashboardVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TMDBTheme {
                    PersonDashboardScreen(
                        viewModel = viewModel,
                        onPersonClick = { personId ->
                            findNavController().navigate(
                                R.id.person_details_nav_graph,
                                Bundle().apply { putInt("person_id", personId) }
                            )
                        },
                        onSeeAllClick = { category ->
                            val action = PersonDashboardFragmentDirections.actionPersonDashboardFragmentToPersonListFragment(category)
                            findNavController().navigateSafe(action)
                        }
                    )
                }
            }
        }
    }
}
