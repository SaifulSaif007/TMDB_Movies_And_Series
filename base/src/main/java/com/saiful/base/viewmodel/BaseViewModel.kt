package com.saiful.base.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _showMessage = MutableSharedFlow<String>()
    val showMessage = _showMessage.asSharedFlow()

    fun updateLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    fun emitMessage(message : String?){
        viewModelScope.launch {
            message?.let {
                Log.d("BaseViewModel", "emitMessage: $message")
                _showMessage.emit(it)
            }
        }
    }
}
