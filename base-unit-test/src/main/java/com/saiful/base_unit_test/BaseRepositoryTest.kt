package com.saiful.base_unit_test

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericError
import java.io.IOException


abstract class BaseRepositoryTest : BaseTest() {

    protected val apiError = BaseResponse.ApiError(
        errorBody = GenericError(
            status_code = "400",
            status_message = "exception"
        ),
        code = 400
    )

    protected val networkError = BaseResponse.NetworkError(
        error = IOException("exception")
    )

    protected val unknownError = BaseResponse.UnknownError(
        error = Throwable("exception")
    )


}