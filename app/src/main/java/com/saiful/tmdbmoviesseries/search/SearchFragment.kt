package com.saiful.tmdbmoviesseries.search

import android.os.Bundle
import android.util.Log
import android.view.*
import com.google.android.material.tabs.TabLayoutMediator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.tmdbmoviesseries.MainActivity
import com.saiful.tmdbmoviesseries.R
import com.saiful.tmdbmoviesseries.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_search

    override fun getViewModel(): BaseViewModel? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun initOnCreateView() {

        val viewPager = bindingView.pager
        val tabLayout = bindingView.tabLayout
        var searchedText = ""

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, searchedText)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Movie"
                1 -> tab.text = "Series"
                2 -> tab.text = "Favorite"
            }
        }.attach()

        //first time
        bindingView.searchView.show()

        bindingView.apply {
            searchView.editText.setOnEditorActionListener { p0, _, _ ->
                val textView = p0?.text.toString()
                searchView.hide()
                searchBar.text = textView
                searchedText = searchBar.text.toString()
                val newAdapter =
                    ViewPagerAdapter(childFragmentManager, lifecycle, searchBar.text.toString())
                viewPager.adapter = newAdapter

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

        childFragmentManager.setFragmentResultListener("movieId", this) { _, bundle ->
            val movieId = bundle.getInt("movieId")
            Log.d("SearchFragment", "movieId: $movieId")
        }
    }

}