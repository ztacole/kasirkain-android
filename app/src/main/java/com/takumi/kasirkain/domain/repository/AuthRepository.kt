package com.takumi.kasirkain.domain.repository

interface AuthRepository {
    suspend fun login(username: String, password: String): String
}