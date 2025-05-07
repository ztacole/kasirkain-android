package com.takumi.kasirkain.data.remote.response

data class BaseResponse<T>(
    val status: String,
    val data: T
)
