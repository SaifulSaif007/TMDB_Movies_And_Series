package com.saiful.tvshows.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.saiful.base.util.ItemDecorator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.shared.utils.navigateSafe
import com.saiful.tvshows.R
import com.saiful.tvshows.databinding.FragmentShowsListBinding
import com.saiful.tvshows.view.adapter.ShowsListLoadAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ShowListFragment : BaseFragment<FragmentShowsListBinding>() {

    private val viewModel: ListVM by viewModels()
    private val args: ShowListFragmentArgs by navArgs()
    private val showAdapter = ShowsListLoadAdapter(::showsItemClick)

    @Inject
    lateinit var itemDecorator: ItemDecorator

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentShowsListBinding {
        return FragmentShowsListBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_shows_list

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {
        (activity as AppCompatActivity).supportActionBar?.title = args.showCategory.value

        bindingView.showsListRecycler.apply {
            addItemDecoration(itemDecorator)
            adapter = showAdapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.showsList.collect {
                showAdapter.submitData(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.selectedCategory(args.showCategory)
    }

    private fun showsItemClick(showId: Int) {
        findNavController().navigateSafe(
            ShowListFragmentDirections.actionShowListFragmentToTvShowsDetailsFragment(
                showId
            )
        )
    }
}