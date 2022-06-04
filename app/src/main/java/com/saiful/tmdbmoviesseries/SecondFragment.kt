package com.saiful.tmdbmoviesseries

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.saiful.base.view.BaseFragment
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.tmdbmoviesseries.databinding.FragmentSecondBinding

class SecondFragment : BaseFragment<FragmentSecondBinding>() {
    override fun layoutId(): Int = R.layout.fragment_second

    override fun getViewModel(): BaseViewModel? = null

    override fun layoutInflater(inflater: LayoutInflater, container: ViewGroup?): FragmentSecondBinding {
        return FragmentSecondBinding.inflate(inflater, container, false)
    }

    override fun initOnCreateView() {
        bindingView.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }
}