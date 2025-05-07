package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): String {
        return repository.login(username, password)
    }
}