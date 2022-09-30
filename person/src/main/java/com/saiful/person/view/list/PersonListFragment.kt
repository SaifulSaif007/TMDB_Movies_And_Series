package com.saiful.person.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.R
import com.saiful.person.databinding.FragmentPersonListBinding

class PersonListFragment : BaseFragment<FragmentPersonListBinding>() {

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonListBinding {
        return FragmentPersonListBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_person_list

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {

    }
}