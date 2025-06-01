package com.takumi.kasirkain.presentation.common.state

sealed interface ScannerState {
    object Idle : ScannerState
    object RequestPermission : ScannerState
    object Active : ScannerState
    object PermissionDenied : ScannerState
}