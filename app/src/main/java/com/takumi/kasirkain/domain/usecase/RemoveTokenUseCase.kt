package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.repository.AuthRepository
import javax.inject.Inject

class RemoveTokenUseCase @Inject constructor(
    private val tokenRepository: AuthRepository
) {
    suspend operator fun invoke() {
        return tokenRepository.logout()
    }
}