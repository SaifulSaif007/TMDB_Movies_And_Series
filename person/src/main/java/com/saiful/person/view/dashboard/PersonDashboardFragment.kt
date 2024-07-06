package com.saiful.person.view.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.saiful.base.util.ItemDecorator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.R
import com.saiful.person.databinding.FragmentPersonDashboardBinding
import com.saiful.person.model.PersonCategory
import com.saiful.person.view.adapter.PersonDashboardAdapter
import com.saiful.shared.utils.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class PersonDashboardFragment : BaseFragment<FragmentPersonDashboardBinding>() {

    private val viewModel: PersonDashboardVM by viewModels()
    private val popularAdapter = PersonDashboardAdapter(::onClickPerson)
    private val popularAdapterSecond = PersonDashboardAdapter(::onClickPerson)
    private val trendingAdapter = PersonDashboardAdapter(::onClickPerson)
    private val trendingAdapterSecond = PersonDashboardAdapter(::onClickPerson)

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

        viewModel.trendingPerson()
        viewModel.popularPerson()

        seeAllClickHandler()

        bindingView.apply {
            popularPersonRecycler.apply {
                adapter = popularAdapter
                addItemDecoration(itemDecorator)
            }

            popularPersonRecyclerSecond.apply {
                adapter = popularAdapterSecond
                addItemDecoration(itemDecorator)
            }

            trendingPersonRecycler.apply {
                adapter = trendingAdapter
                addItemDecoration(itemDecorator)
            }

            trendingPersonRecyclerSecond.apply {
                adapter = trendingAdapterSecond
                addItemDecoration(itemDecorator)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.popularPersonList.collect {
                it?.results?.let { list -> popularAdapter.submitList(list.subList(0, 9)) }
                it?.results?.let { list -> popularAdapterSecond.submitList(list.subList(10, 19)) }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.trendingPersonList.collect {
                it?.results?.let { list -> trendingAdapter.submitList(list.subList(0, 9)) }
                it?.results?.let { list -> trendingAdapterSecond.submitList(list.subList(10, 19)) }
            }
        }
    }

    private fun seeAllClickHandler() {
        bindingView.apply {
            popularSeeAllLabel.setOnClickListener {
                val action =
                    PersonDashboardFragmentDirections.actionPersonDashboardFragmentToPersonListFragment(
                        PersonCategory.POPULAR
                    )
                findNavController().navigateSafe(action)
            }

            trendingSeeAllLabel.setOnClickListener {
                val action =
                    PersonDashboardFragmentDirections.actionPersonDashboardFragmentToPersonListFragment(
                        PersonCategory.TRENDING
                    )
                findNavController().navigateSafe(action)
            }
        }
    }

    private fun onClickPerson(personId: Int) {
        findNavController().navigate(
            R.id.person_details_nav_graph, bundleOf("person_id" to personId)
        )
    }

}