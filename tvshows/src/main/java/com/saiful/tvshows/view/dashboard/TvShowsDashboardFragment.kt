package com.saiful.tvshows.view.dashboard

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.saiful.base.util.ItemDecorator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.tvshows.R
import com.saiful.tvshows.databinding.FragmentTvshowsDashboardBinding
import com.saiful.tvshows.view.adapter.ShowsDashboardAdapter
import com.saiful.tvshows.view.adapter.ShowsSliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class TvShowsDashboardFragment : BaseFragment<FragmentTvshowsDashboardBinding>() {

    private lateinit var viewPager: ViewPager2
    private lateinit var mPageChangeHandler: Handler
    private val viewModel: ShowsDashboardVM by viewModels()
    private val trendingShowsAdapter = ShowsDashboardAdapter(::tvShowsItemClick)
    private val onAirShowsAdapter = ShowsDashboardAdapter(::tvShowsItemClick)
    private val topRatedShowsAdapter = ShowsDashboardAdapter(::tvShowsItemClick)
    private val popularShowsAdapter = ShowsDashboardAdapter(::tvShowsItemClick)

    @Inject
    lateinit var itemDecorator: ItemDecorator

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTvshowsDashboardBinding {
        return FragmentTvshowsDashboardBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_tvshows_dashboard

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {

        setUpImageSlider()

        bindingView.trendingShowRecycler.apply {
            addItemDecoration(itemDecorator)
            adapter = trendingShowsAdapter
        }

        bindingView.onAirShowRecycler.apply {
            addItemDecoration(itemDecorator)
            adapter = onAirShowsAdapter
        }

        bindingView.topRatedShowRecycler.apply {
            addItemDecoration(itemDecorator)
            adapter = topRatedShowsAdapter
        }

        bindingView.popularShowRecycler.apply {
            addItemDecoration(itemDecorator)
            adapter = popularShowsAdapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.trendingShowsList.collect { trending ->
                trending?.results?.let { shows ->
                    trendingShowsAdapter.submitList(shows)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.onAirShowsList.collect { onAir ->
                onAir?.results?.let { shows ->
                    onAirShowsAdapter.submitList(shows)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.topRatedShowsList.collect { topRated ->
                topRated?.results?.let { shows ->
                    topRatedShowsAdapter.submitList(shows)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.popularShowsList.collect { popular ->
                popular?.results?.let { shows ->
                    popularShowsAdapter.submitList(shows)
                }
            }
        }
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

    private fun tvShowsItemClick(showsId: Int) {
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