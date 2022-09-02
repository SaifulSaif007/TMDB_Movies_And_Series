package com.saiful.tvshows.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.tvshows.R
import com.saiful.tvshows.databinding.FragmentTvshowsDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowsDashboardFragment : BaseFragment<FragmentTvshowsDashboardBinding>() {
    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTvshowsDashboardBinding {
        return FragmentTvshowsDashboardBinding.inflate(inflater,container,false)
    }

    override fun layoutId(): Int  = R.layout.fragment_tvshows_dashboard

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {

    }
}