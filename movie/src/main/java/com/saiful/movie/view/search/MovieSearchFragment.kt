package com.saiful.movie.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.R
import com.saiful.movie.databinding.FragmentMovieSearchBinding

class MovieSearchFragment : BaseFragment<FragmentMovieSearchBinding>() {
    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieSearchBinding {
        return FragmentMovieSearchBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_movie_search

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {
        val data = arguments?.getString("data")
        bindingView.textView7.text = data
        println(data)
    }

}