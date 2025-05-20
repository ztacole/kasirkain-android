package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.data.local.entity.TokenEntity
import com.takumi.kasirkain.domain.repository.TokenRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val repository: TokenRepository
) {
    suspend operator fun invoke(): String? {
        return repository.getToken()
    }
}