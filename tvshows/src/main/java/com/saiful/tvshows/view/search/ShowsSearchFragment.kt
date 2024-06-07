package com.saiful.tvshows.view.search

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
import com.saiful.shared.utils.BundleKeyS.SHOW_ID
import com.saiful.shared.utils.RequestKeys.SERIES_REQUEST_KEY
import com.saiful.shared.view.SharedSearchVM
import com.saiful.tvshows.R
import com.saiful.tvshows.databinding.FragmentShowsSearchBinding
import com.saiful.tvshows.view.adapter.ShowsListLoadAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ShowsSearchFragment : BaseFragment<FragmentShowsSearchBinding>() {

    @Inject
    lateinit var itemDecorator: ItemDecorator

    private val viewModel: SearchVM by viewModels()
    private val sharedVM: SharedSearchVM by activityViewModels()
    private val showAdapter = ShowsListLoadAdapter(::showsItemClick)

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentShowsSearchBinding {
        return FragmentShowsSearchBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_shows_search

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {
        bindingView.apply {
            showsListRecycler.apply {
                setHasFixedSize(true)
                addItemDecoration(itemDecorator)
                adapter = showAdapter
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.showsList.collectLatest {
                showAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            sharedVM.searchedQuery.collectLatest { query ->
                viewModel.searchShow(query)
            }
        }

        showAdapter.addLoadStateListener { loaderState ->
            val isListEmpty =
                loaderState.source.refresh is LoadState.NotLoading && showAdapter.itemCount == 0

            bindingView.emptyShowView.root.visibility = if (isListEmpty) VISIBLE else GONE
        }
    }

    private fun showsItemClick(showId: Int) {
        val bundle = Bundle()
        bundle.putInt(SHOW_ID, showId)
        setFragmentResult(SERIES_REQUEST_KEY, bundle)
    }
}