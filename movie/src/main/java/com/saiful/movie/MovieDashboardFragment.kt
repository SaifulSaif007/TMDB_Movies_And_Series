package com.saiful.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.databinding.FragmentMovieDashboardBinding

class MovieDashboardFragment : BaseFragment<FragmentMovieDashboardBinding>(){

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDashboardBinding {
        return FragmentMovieDashboardBinding.inflate(inflater,container,false)
    }

    override fun layoutId(): Int = R.layout.fragment_movie_dashboard

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {
    }

}