package com.saiful.base.viewmodel

import androidx.lifecycle.viewModelScope
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericError
import com.saiful.base.network.model.GenericResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseOpsViewModel : BaseViewModel() {

    protected fun executeRestCodeBlock(
        operationTag: String = String(),
        codeBlock: suspend () -> GenericResponse<Any>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val data = codeBlock()) {
                is BaseResponse.Success ->
                    onSuccessResponse(operationTag, data)
                is BaseResponse.ApiError ->
                    onApiError(operationTag, data)
                is BaseResponse.NetworkError ->
                    onNetworkError(operationTag, data)
                is BaseResponse.UnknownError ->
                    onUnknownError(operationTag, data)
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