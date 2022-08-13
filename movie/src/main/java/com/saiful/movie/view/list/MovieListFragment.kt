package com.saiful.movie.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.saiful.base.util.ItemDecorator
import com.saiful.base.util.navigateSafe
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.R
import com.saiful.movie.databinding.FragmentMovieListBinding
import com.saiful.movie.view.adapter.MovieListLoadAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MovieListFragment : BaseFragment<FragmentMovieListBinding>() {

    @Inject
    lateinit var itemDecorator: ItemDecorator
    private val viewModel: ListVM by viewModels()
    private val args: MovieListFragmentArgs by navArgs()

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieListBinding {
        return FragmentMovieListBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_movie_list

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {
        (activity as AppCompatActivity).supportActionBar?.title = args.movieCategory.value
        val movieAdapter = MovieListLoadAdapter(::movieItemClick)
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.selectedCategory(args.movieCategory)
    }

    private fun movieItemClick(movieId: Int) {
        findNavController().navigateSafe(MovieListFragmentDirections.actionListToDetails(movieId))
    }
}