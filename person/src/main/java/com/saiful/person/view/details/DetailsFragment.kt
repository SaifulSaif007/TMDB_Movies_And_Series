package com.saiful.person.view.details

import android.view.LayoutInflater
import android.view.ViewGroup
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.R
import com.saiful.person.databinding.FragmentPersonDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentPersonDetailsBinding>() {
    override fun layoutInflater(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonDetailsBinding {
        return FragmentPersonDetailsBinding.inflate(inflater, container, false)
    }

    override fun layoutId(): Int = R.layout.fragment_person_details

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {
    }
}