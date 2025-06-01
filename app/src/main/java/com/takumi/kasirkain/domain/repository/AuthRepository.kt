package com.takumi.kasirkain.domain.repository

import com.takumi.kasirkain.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): String
    suspend fun profile(): User
    suspend fun getToken(): String?
    suspend fun logout()
}