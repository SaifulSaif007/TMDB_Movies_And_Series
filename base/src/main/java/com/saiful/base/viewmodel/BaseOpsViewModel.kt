package com.saiful.base.viewmodel

import androidx.lifecycle.viewModelScope
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericError
import com.saiful.base.network.model.GenericResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseOpsViewModel : BaseViewModel() {

    protected fun executeRestCodeBlock(
        operationTag: String = String(),
        showLoader: Boolean = true,
        codeBlock: suspend () -> GenericResponse<Any>
    ) {
        viewModelScope.launch {
            if (showLoader) updateLoading(true)
            val result = withContext(Dispatchers.IO) { codeBlock() }
            if (showLoader) updateLoading(false)

            when (result) {
                is BaseResponse.Success ->
                    onSuccessResponse(operationTag, result)
                is BaseResponse.ApiError ->
                    onApiError(operationTag, result)
                is BaseResponse.NetworkError ->
                    onNetworkError(operationTag, result)
                is BaseResponse.UnknownError ->
                    onUnknownError(operationTag, result)
            }
        }
    }

    abstract fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>)

    private fun onApiError(operationTag: String, result: BaseResponse.ApiError<GenericError>) {
        emitMessage(result.errorBody.status_message)
    }

    private fun onNetworkError(operationTag: String, result: BaseResponse.NetworkError) {
        emitMessage(result.error.message.toString())
    }

    private fun onUnknownError(operationTag: String, result: BaseResponse.UnknownError) {
        emitMessage(result.error?.message.toString())
    }

}
