package com.saiful.person.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.saiful.base.util.ItemDecorator
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.R
import com.saiful.person.databinding.FragmentPersonListBinding
import com.saiful.person.view.adapter.PersonListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class PersonListFragment : BaseFragment<FragmentPersonListBinding>() {

    private val args: PersonListFragmentArgs by navArgs()
    private val viewModel: PersonListVM by viewModels()
    private val personAdapter = PersonListAdapter(::personItemClick)

    @Inject
    lateinit var itemDecorator: ItemDecorator

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonListBinding {
        return FragmentPersonListBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_person_list

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {
        (activity as AppCompatActivity).supportActionBar?.title = args.category.value

        bindingView.apply {
            personListRecycler.apply {
                adapter = personAdapter
                addItemDecoration(itemDecorator)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.personList.collect {
                personAdapter.submitData(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.selectedCategory(args.category)
    }

    private fun personItemClick(personId: Int) {

    }
}