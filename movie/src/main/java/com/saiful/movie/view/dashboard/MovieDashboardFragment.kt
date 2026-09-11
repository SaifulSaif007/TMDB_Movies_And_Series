package com.saiful.movie.view.dashboard

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.saiful.base.util.ItemDecorator
import com.saiful.shared.utils.navigateSafe
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.R
import com.saiful.movie.databinding.FragmentMovieDashboardBinding
import com.saiful.movie.model.MovieCategory
import com.saiful.movie.view.adapter.MovieDashboardAdapter
import com.saiful.movie.view.adapter.SliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.map { it.popularMovies }.distinctUntilChanged().collectLatest { movies ->
                        popularMovieAdapter.submitList(movies)
                    }
                }
                launch {
                    viewModel.uiState.map { it.nowPlayingMovies }.distinctUntilChanged().collectLatest { movies ->
                        nowPlayingMovieAdapter.submitList(movies)
                    }
                }
                launch {
                    viewModel.uiState.map { it.topRatedMovies }.distinctUntilChanged().collectLatest { movies ->
                        topRatedMovieAdapter.submitList(movies)
                    }
                }
                launch {
                    viewModel.uiState.map { it.upcomingMovies }.distinctUntilChanged().collectLatest { movies ->
                        upcomingMovieAdapter.submitList(movies)
                    }
                }
            }
        }
    }

    private fun setUpImageSlider() {
        viewPager = bindingView.imageSlider
        mPageChangeHandler = Handler(Looper.getMainLooper())
        
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(25))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.map { it.sliderMovies }.distinctUntilChanged().collectLatest { movies ->
                    if (movies.isNotEmpty()) {
                        val pageAdapter = SliderAdapter(movies.toMutableList(), viewPager, ::movieItemClick)
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
        findNavController().navigateSafe(
            MovieDashboardFragmentDirections.actionDashboardToDetails(
                movieId
            )
        )
    }

    private val runnable = Runnable {
        if (viewPager.adapter != null && viewPager.adapter!!.itemCount > 0) {
            viewPager.currentItem = (viewPager.currentItem + 1) % viewPager.adapter!!.itemCount
        }
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
