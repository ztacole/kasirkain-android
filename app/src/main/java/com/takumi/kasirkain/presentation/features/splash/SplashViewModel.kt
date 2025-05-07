package com.takumi.kasirkain.presentation.features.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takumi.kasirkain.domain.usecase.GetTokenUseCase
import com.takumi.kasirkain.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase
) : ViewModel() {

    private val _isLoading = mutableStateOf(true)
    private val _startDestination = mutableStateOf("")

    val isLoading = _isLoading
    val startDestination = _startDestination

    init {
        viewModelScope.launch {
            val token = getTokenUseCase()
            delay(1000)
            if (token != null) _startDestination.value = Screen.Home.route
            else _startDestination.value = Screen.Auth.route
            _isLoading.value = false
        }
    }
}