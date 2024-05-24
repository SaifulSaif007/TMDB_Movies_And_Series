package com.saiful.tmdbmoviesseries.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saiful.movie.view.search.MovieSearchFragment
import com.saiful.shared.utils.AppConstants.SEARCHED_QUERY
import com.saiful.tvshows.view.search.ShowsSearchFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val data: String
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString(SEARCHED_QUERY, data)
        return when (position) {
            0 -> {
                val fragment = MovieSearchFragment()
                fragment.arguments = bundle
                fragment
            }

            1 -> {
                val fragment = ShowsSearchFragment()
                fragment.arguments = bundle
                fragment

            }

            else -> {
                val fragment = MovieSearchFragment()
                fragment.arguments = bundle
                fragment
            }
        }
    }

}