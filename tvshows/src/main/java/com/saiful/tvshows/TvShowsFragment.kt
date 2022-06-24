package com.saiful.tvshows

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.tvshows.databinding.FragmentTvshowsDashboardBinding

class TvShowsFragment : BaseFragment<FragmentTvshowsDashboardBinding>() {
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