package com.takumi.kasirkain.domain.repository

import com.takumi.kasirkain.data.local.entity.TokenEntity

interface TokenRepository {
    suspend fun getToken(): TokenEntity?
}