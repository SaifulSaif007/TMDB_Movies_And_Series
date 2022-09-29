package com.saiful.person.view.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.saiful.base.util.ItemDecorator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.R
import com.saiful.person.databinding.FragmentPersonDashboardBinding
import com.saiful.person.view.adapter.PersonDashboardAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class PersonDashboardFragment : BaseFragment<FragmentPersonDashboardBinding>() {

    private val viewModel: PersonDashboardVM by viewModels()
    private val popularAdapter = PersonDashboardAdapter()

    @Inject
    lateinit var itemDecorator: ItemDecorator

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonDashboardBinding {
        return FragmentPersonDashboardBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_person_dashboard

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {
        val gridlayoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        bindingView.apply {
            popularPersonRecycler.apply {
                layoutManager = gridlayoutManager
                adapter = popularAdapter
                addItemDecoration(itemDecorator)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.popularPersonList.collect {
                it?.results?.let { list -> popularAdapter.submitList(list) }
            }
        }
    }

}