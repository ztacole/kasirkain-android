package com.takumi.kasirkain.data.repository

import com.takumi.kasirkain.data.local.dao.TokenDao
import com.takumi.kasirkain.data.local.entity.TokenEntity
import com.takumi.kasirkain.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDao: TokenDao
) : TokenRepository {
    override suspend fun getToken(): TokenEntity? {
        return tokenDao.getToken()
    }
}