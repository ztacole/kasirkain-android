package com.takumi.kasirkain.data.repository

import com.takumi.kasirkain.data.local.LocalDataSource
import com.takumi.kasirkain.data.local.dao.TokenDao
import com.takumi.kasirkain.data.local.entity.TokenEntity
import com.takumi.kasirkain.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : TokenRepository {
    override suspend fun getToken(): String? {
        return localDataSource.getToken()
    }
}