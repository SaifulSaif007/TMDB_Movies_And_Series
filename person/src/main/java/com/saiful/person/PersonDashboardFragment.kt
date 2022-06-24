package com.saiful.person

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.databinding.FragmentPersonDashboardBinding

class PersonDashboardFragment : BaseFragment<FragmentPersonDashboardBinding>() {
    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonDashboardBinding {
        return FragmentPersonDashboardBinding.inflate(inflater,container,false)
    }

    override fun layoutId(): Int = R.layout.fragment_person_dashboard

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {
    }

}