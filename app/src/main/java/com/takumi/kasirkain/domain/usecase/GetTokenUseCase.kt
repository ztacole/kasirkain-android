package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.repository.AuthRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): String? {
        return repository.getToken()
    }
}