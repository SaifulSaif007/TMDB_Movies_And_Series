package com.saiful.tmdbmoviesseries.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saiful.movie.view.search.MovieSearchFragment
import com.saiful.person.view.search.PersonSearchFragment
import com.saiful.tvshows.view.search.ShowsSearchFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MovieSearchFragment()
            1 -> ShowsSearchFragment()
            else -> PersonSearchFragment()
        }
    }

}