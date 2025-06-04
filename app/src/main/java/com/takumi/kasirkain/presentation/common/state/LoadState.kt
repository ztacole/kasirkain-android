package com.takumi.kasirkain.presentation.common.state

sealed class LoadState {
    object NotLoading : LoadState()
    object Loading : LoadState()
    data class Error(val message: String) : LoadState()
}
