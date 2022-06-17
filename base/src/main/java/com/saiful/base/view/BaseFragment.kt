package com.saiful.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.resources.Compatibility.Api21Impl.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import androidx.viewbinding.ViewBindings
import com.saiful.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private lateinit var viewDataBinding: VB
    protected val bindingView: VB
        get() = viewDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layoutId = layoutId()
        viewDataBinding = layoutInflater(inflater, container)

        initOnCreateView()

        val viewModel = getViewModel()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel?.showMessage?.collect { msg->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        return viewDataBinding.root
    }

    @LayoutRes
    protected abstract fun layoutId(): Int

    protected abstract fun getViewModel(): BaseViewModel?

    protected abstract fun initOnCreateView()

    protected abstract fun layoutInflater(inflater: LayoutInflater, container: ViewGroup?): VB

}
