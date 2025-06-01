package com.takumi.kasirkain.presentation.common.state

import java.io.IOException

sealed interface UiEvent {
    data class ShowToast(val message: String) : UiEvent
}

fun Exception.toErrorMessage() = when (this) {
    is IOException -> "Koneksi bermasalah"
    else -> message ?: "Terjadi kesalahan"
}