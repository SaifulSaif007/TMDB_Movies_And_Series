package com.saiful.movie.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.saiful.base.util.ItemDecorator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.R
import com.saiful.movie.databinding.FragmentMovieSearchBinding
import com.saiful.movie.view.adapter.MovieListLoadAdapter
import com.saiful.shared.utils.BundleKeyS.MOVIE_ID
import com.saiful.shared.utils.RequestKeys.MOVIE_REQUEST_KEY
import com.saiful.shared.view.SharedSearchVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MovieSearchFragment : BaseFragment<FragmentMovieSearchBinding>() {

    @Inject
    lateinit var itemDecorator: ItemDecorator
    private val viewModel: SearchVM by viewModels()
    private val sharedVM: SharedSearchVM by activityViewModels()
    private val movieAdapter = MovieListLoadAdapter(::movieItemClick)

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieSearchBinding {
        return FragmentMovieSearchBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_movie_search

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {

        bindingView.apply {
            movieListRecycler.apply {
                setHasFixedSize(true)
                adapter = movieAdapter
                addItemDecoration(itemDecorator)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.movieList.collectLatest {
                movieAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            sharedVM.searchedQuery.collectLatest { query ->
                viewModel.searchMovie(query)
            }
        }

        movieAdapter.addLoadStateListener { loaderState ->
            val isListEmpty =
                loaderState.source.refresh is LoadState.NotLoading && movieAdapter.itemCount == 0

            bindingView.emptyMovie.root.visibility = if (isListEmpty) VISIBLE else GONE
        }

    }

    override fun onResume() {
        super.onResume()
        movieAdapter.addLoadStateListener { loaderState ->
            println(loaderState)
        }
    }

    private fun movieItemClick(movieId: Int) {
        val bundle = Bundle()
        bundle.putInt(MOVIE_ID, movieId)
        setFragmentResult(MOVIE_REQUEST_KEY, bundle)
    }
}