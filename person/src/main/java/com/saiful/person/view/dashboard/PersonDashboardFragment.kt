package com.saiful.person.view.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.R
import com.saiful.person.databinding.FragmentPersonDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonDashboardFragment : BaseFragment<FragmentPersonDashboardBinding>() {

    private val viewModel: PersonDashboardVM by viewModels()

    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonDashboardBinding {
        return FragmentPersonDashboardBinding.inflate(inflater,container,false)
    }

    override fun layoutId(): Int = R.layout.fragment_person_dashboard

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initOnCreateView() {
    }

}