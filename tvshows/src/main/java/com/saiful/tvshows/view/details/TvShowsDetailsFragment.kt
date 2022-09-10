package com.saiful.tvshows.view.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.tvshows.R
import com.saiful.tvshows.databinding.FragmentTvshowDetailsBinding

class TvShowsDetailsFragment : BaseFragment<FragmentTvshowDetailsBinding>() {

    private val args: TvShowsDetailsFragmentArgs by navArgs()

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTvshowDetailsBinding {
        return FragmentTvshowDetailsBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_tvshow_details

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {

    }
}