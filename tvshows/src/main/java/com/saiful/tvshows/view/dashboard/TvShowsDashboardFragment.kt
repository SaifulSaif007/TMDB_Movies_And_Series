package com.saiful.tvshows.view.dashboard

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.tvshows.R
import com.saiful.tvshows.databinding.FragmentTvshowsDashboardBinding
import com.saiful.tvshows.view.adapter.ShowsSliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TvShowsDashboardFragment : BaseFragment<FragmentTvshowsDashboardBinding>() {

    private lateinit var viewPager: ViewPager2
    private lateinit var mPageChangeHandler: Handler
    private val viewModel: ShowsDashboardVM by viewModels()

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTvshowsDashboardBinding {
        return FragmentTvshowsDashboardBinding.inflate(inflater,container,false)
    }

    override fun layoutId(): Int  = R.layout.fragment_tvshows_dashboard

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {

        setUpImageSlider()
    }

    private fun setUpImageSlider() {
        viewPager = bindingView.imageSlider
        mPageChangeHandler = Handler(Looper.getMainLooper())
        val pageAdapter = ShowsSliderAdapter(viewModel.sliderList, viewPager, ::tvShowsItemClick)

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(25))
        transformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.17f
        }
        viewPager.setPageTransformer(transformer)

        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mPageChangeHandler.removeCallbacks(runnable)
                mPageChangeHandler.postDelayed(runnable, 3000)
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.sliderLoaded.collect {
                if (it) {
                    viewPager.apply {
                        adapter = pageAdapter
                        offscreenPageLimit = 3
                        clipChildren = false
                        clipToPadding = false
                    }
                }
            }
        }
    }

    private fun tvShowsItemClick(showsId : Int){
        //
    }

    private val runnable = Runnable {
        viewPager.currentItem = viewPager.currentItem + 1
    }

    override fun onPause() {
        super.onPause()
        mPageChangeHandler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        mPageChangeHandler.postDelayed(runnable, 5000)
    }

}