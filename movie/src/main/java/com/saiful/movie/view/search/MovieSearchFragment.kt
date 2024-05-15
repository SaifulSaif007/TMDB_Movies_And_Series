package com.saiful.movie.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.saiful.base.util.ItemDecorator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.R
import com.saiful.movie.databinding.FragmentMovieSearchBinding
import com.saiful.movie.view.adapter.MovieListLoadAdapter
import com.saiful.movie.view.list.MovieListFragmentDirections
import com.saiful.shared.utils.AppConstants.SEARCHED_QUERY
import com.saiful.shared.utils.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MovieSearchFragment : BaseFragment<FragmentMovieSearchBinding>() {

    @Inject
    lateinit var itemDecorator: ItemDecorator
    private val viewModel: SearchVM by viewModels()
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
        val data = arguments?.getString(SEARCHED_QUERY) ?: ""
        viewModel.searchMovie(data)

        bindingView.apply {
            movieListRecycler.apply {
                setHasFixedSize(true)
                adapter = movieAdapter
                addItemDecoration(itemDecorator)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.movieList.collect {
                movieAdapter.submitData(it)
            }
        }

    }

    private fun movieItemClick(movieId: Int) {
        findNavController().navigateSafe(MovieListFragmentDirections.actionListToDetails(movieId))
    }
}