package com.saiful.tmdbmoviesseries.search

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.saiful.base.util.navigation.*
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.shared.utils.BundleKeyS.MOVIE_ID
import com.saiful.shared.utils.BundleKeyS.PERSON_ID
import com.saiful.shared.utils.BundleKeyS.SHOW_ID
import com.saiful.shared.utils.RequestKeys.MOVIE_REQUEST_KEY
import com.saiful.shared.utils.RequestKeys.PERSON_REQUEST_KEY
import com.saiful.shared.utils.RequestKeys.SERIES_REQUEST_KEY
import com.saiful.shared.view.SharedSearchVM
import com.saiful.tmdb2.MainActivity
import com.saiful.tmdb2.R
import com.saiful.tmdb2.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    @Inject
    lateinit var movieModuleNavigation: MovieModuleNavigation

    @Inject
    lateinit var showModuleNavigation: TvShowModuleNavigation

    @Inject
    lateinit var personModuleNavigation: PersonModuleNavigation

    private val viewmodel: SharedSearchVM by activityViewModels()

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_search

    override fun getViewModel(): BaseViewModel = viewmodel

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun initOnCreateView() {

        val viewPager = bindingView.pager
        val tabLayout = bindingView.tabLayout

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Movie"
                1 -> tab.text = "Series"
                2 -> tab.text = "Favorite"
            }
        }.attach()

        bindingView.apply {
            searchView.editText.setOnEditorActionListener { p0, _, _ ->
                val inputText = p0?.text.toString()
                searchView.hide()
                searchBar.textView.text = inputText
                viewmodel.updateSearchQuery(inputText)
                true
            }

            searchView.editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    (activity as MainActivity).toggleBottomBarVisibility(false)
                } else {
                    (activity as MainActivity).toggleBottomBarVisibility(true)
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        childFragmentManager.setFragmentResultListener(MOVIE_REQUEST_KEY, this) { _, bundle ->
            val movieId = bundle.getInt(MOVIE_ID)
            movieModuleNavigation.navigateMovieDetails(movieId, navController)
        }

        childFragmentManager.setFragmentResultListener(SERIES_REQUEST_KEY, this) { _, bundle ->
            val showId = bundle.getInt(SHOW_ID)
            showModuleNavigation.navigateToShowDetails(showId, navController)
        }

        childFragmentManager.setFragmentResultListener(PERSON_REQUEST_KEY, this) { _, bundle ->
            val personId = bundle.getInt(PERSON_ID)
            personModuleNavigation.navigateToPersonDetails(personId, navController)
        }
    }
}