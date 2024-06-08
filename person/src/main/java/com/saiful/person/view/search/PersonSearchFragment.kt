package com.saiful.person.view.search

import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.saiful.base.util.ItemDecorator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.R
import com.saiful.person.databinding.FragmentPersonSearchBinding
import com.saiful.person.view.adapter.PersonListAdapter
import com.saiful.shared.utils.BundleKeyS.PERSON_ID
import com.saiful.shared.utils.RequestKeys.PERSON_REQUEST_KEY
import com.saiful.shared.view.SharedSearchVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class PersonSearchFragment : BaseFragment<FragmentPersonSearchBinding>() {

    private val viewModel: SearchVM by viewModels()
    private val sharedVM: SharedSearchVM by activityViewModels()
    private val personAdapter = PersonListAdapter(::personItemClick)

    @Inject
    lateinit var itemDecorator: ItemDecorator

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonSearchBinding {
        return FragmentPersonSearchBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_person_search

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {
        bindingView.apply {
            personListRecycler.apply {
                adapter = personAdapter
                addItemDecoration(itemDecorator)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.personList.collectLatest {
                personAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            sharedVM.searchedQuery.collectLatest { query ->
                viewModel.searchPersons(query)
            }
        }

        personAdapter.addLoadStateListener { loaderState ->
            val isListEmpty =
                loaderState.source.refresh is LoadState.NotLoading && personAdapter.itemCount == 0

            bindingView.emptyPerson.root.visibility = if (isListEmpty) View.VISIBLE else View.GONE
        }

    }

    private fun personItemClick(personId: Int) {
        val bundle = Bundle()
        bundle.putInt(PERSON_ID, personId)
        setFragmentResult(PERSON_REQUEST_KEY, bundle)
    }

    override fun onDestroy() {
        println("search -> person fragment destroyed")
        super.onDestroy()
    }
}