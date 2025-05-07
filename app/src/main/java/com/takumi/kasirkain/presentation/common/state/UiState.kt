package com.takumi.kasirkain.presentation.common.state

sealed class UiState<out T> {
    data object Idle: UiState<Nothing>()
    data object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Error(val message: String): UiState<Nothing>()
}