package com.saiful.movie.view.dashboard

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.saiful.base.util.ItemDecorator
import com.saiful.base.util.navigateSafe
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.R
import com.saiful.movie.databinding.FragmentMovieDashboardBinding
import com.saiful.movie.model.MovieCategory
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
    private val popularMovieAdapter = MovieDashboardAdapter(::movieItemClick)
    private val nowPlayingMovieAdapter = MovieDashboardAdapter(::movieItemClick)
    private val topRatedMovieAdapter = MovieDashboardAdapter(::movieItemClick)
    private val upcomingMovieAdapter = MovieDashboardAdapter(::movieItemClick)

    @Inject
    lateinit var itemDecorator: ItemDecorator

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieDashboardBinding {
        return FragmentMovieDashboardBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_movie_dashboard

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {

        setUpImageSlider()

        seeAllClick()

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
            viewModel.nowPlayingMoviesList.collect { nowPlaying ->
                nowPlaying?.results?.let { movies ->
                    nowPlayingMovieAdapter.submitList(movies)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.topRatedMoviesList.collect { topRated ->
                topRated?.results?.let { movies ->
                    topRatedMovieAdapter.submitList(movies)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.upcomingMoviesList.collect { upcoming ->
                upcoming?.results?.let { movies ->
                    upcomingMovieAdapter.submitList(movies)
                }
            }
        }

    }

    private fun setUpImageSlider() {
        viewPager = bindingView.imageSlider
        mPageChangeHandler = Handler(Looper.getMainLooper())
        val pageAdapter = SliderAdapter(viewModel.sliderList, viewPager)

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

    private fun seeAllClick() {
        bindingView.popularSeeAllLabel.setOnClickListener {
            val action =
                MovieDashboardFragmentDirections.actionDashboardToList(MovieCategory.POPULAR)
            findNavController().navigateSafe(action)
        }
        bindingView.topRatedSeeAllLabel.setOnClickListener {
            val action =
                MovieDashboardFragmentDirections.actionDashboardToList(MovieCategory.TOP_RATED)
            findNavController().navigateSafe(action)
        }
        bindingView.nowPlayingSeeAllLabel.setOnClickListener {
            val action =
                MovieDashboardFragmentDirections.actionDashboardToList(MovieCategory.NOW_PLAYING)
            findNavController().navigateSafe(action)
        }
        bindingView.upcomingSeeAllLabel.setOnClickListener {
            val action =
                MovieDashboardFragmentDirections.actionDashboardToList(MovieCategory.UPCOMING)
            findNavController().navigateSafe(action)
        }
    }

    private fun movieItemClick(movieId: Int) {
        findNavController().navigateSafe(R.id.action_dashboard_to_details)
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