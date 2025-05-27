package com.takumi.kasirkain.data.remote.model.response

data class BaseResponse<T>(
    val status: String,
    val data: T
)
