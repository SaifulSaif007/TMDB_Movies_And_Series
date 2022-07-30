package com.saiful.movie.view.dashboard

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
import com.saiful.movie.R
import com.saiful.movie.databinding.FragmentMovieDashboardBinding
import com.saiful.movie.model.ImageSliderItem
import com.saiful.movie.view.adapter.MovieDashboardAdapter
import com.saiful.movie.view.adapter.SliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MovieDashboardFragment : BaseFragment<FragmentMovieDashboardBinding>() {

    private lateinit var viewPager: ViewPager2
    private lateinit var mPageChangeHandler: Handler
    private val viewModel: DashboardVM by viewModels()
    private val popularMovieAdapter = MovieDashboardAdapter()
    private val nowPlayingMovieAdapter = MovieDashboardAdapter()
    private val topRatedMovieAdapter = MovieDashboardAdapter()
    private val upcomingMovieAdapter = MovieDashboardAdapter()
    @Inject lateinit var itemDecorator: ItemDecorator

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDashboardBinding {
        return FragmentMovieDashboardBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_movie_dashboard

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {
        viewPager = bindingView.imageSlider
        mPageChangeHandler = Handler(Looper.getMainLooper())
        val pageAdapter = SliderAdapter(getFakeUrls(), viewPager)

        viewPager.apply {
            adapter = pageAdapter
            offscreenPageLimit = 3
            clipChildren = false
            clipToPadding = false
        }
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(25))
        transformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.17f
        }
        viewPager.setPageTransformer(transformer)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mPageChangeHandler.removeCallbacks(runnable)
                mPageChangeHandler.postDelayed(runnable, 3000)
            }
        })

        bindingView.popularMovieRecycler.apply {
            addItemDecoration(itemDecorator)
            adapter = popularMovieAdapter
        }

        bindingView.nowPlayingMovieRecycler.apply {
            addItemDecoration(itemDecorator)
            adapter = nowPlayingMovieAdapter
        }

        bindingView.topRatedMovieRecycler.apply {
            addItemDecoration(itemDecorator)
            adapter = topRatedMovieAdapter
        }

        bindingView.upcomingMovieRecycler.apply {
            addItemDecoration(itemDecorator)
            adapter = upcomingMovieAdapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.popularMoviesList.collect { popular ->
                popular?.results?.let { movies ->
                    popularMovieAdapter.submitList(movies)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.nowPlayingMoviesList.collect{ nowPlaying->
                nowPlaying?.results?.let { movies->
                    nowPlayingMovieAdapter.submitList(movies)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.topRatedMoviesList.collect{ topRated ->
                topRated?.results?.let { movies->
                    topRatedMovieAdapter.submitList(movies)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.upcomingMoviesList.collect{ upcoming->
                upcoming?.results?.let { movies->
                    upcomingMovieAdapter.submitList(movies)
                }
            }
        }

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
        mPageChangeHandler.postDelayed(runnable, 3000)
    }

    private fun getFakeUrls(): ArrayList<ImageSliderItem> {
        val imageList = mutableListOf<ImageSliderItem>()
        imageList.add(ImageSliderItem("https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8aHVtYW58ZW58MHx8MHx8&w=1000&q=80"))
        imageList.add(ImageSliderItem("https://media.istockphoto.com/photos/image-projection-on-a-woman-using-a-smart-phone-picture-id1341661060?b=1&k=20&m=1341661060&s=170667a&w=0&h=88yONpNbrOHGLAERaW5L0fZvO4BrOE2zO9zkew_wbLk="))
        imageList.add(ImageSliderItem("https://media.istockphoto.com/photos/dandelion-seed-picture-id157681198?k=20&m=157681198&s=612x612&w=0&h=9kNoacRczXI9F7uvOnPaTNXXvT0bGCqjLteBnnjvEb8="))
        return imageList as ArrayList<ImageSliderItem>
    }

}