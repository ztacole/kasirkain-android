package com.takumi.kasirkain.data.local

import com.takumi.kasirkain.data.local.entity.TokenEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val db: AppDatabase
) {
    suspend fun saveToken(token: String) {
        db.tokenDao().saveToken(TokenEntity(token = token))
    }

    suspend fun getToken(): String? = db.tokenDao().getToken()?.token

    suspend fun clearToken() {
        db.tokenDao().clearToken()
    }
}