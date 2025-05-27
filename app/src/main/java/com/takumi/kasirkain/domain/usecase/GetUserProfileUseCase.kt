package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.model.User
import com.takumi.kasirkain.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() : User {
        return authRepository.profile()
    }
}