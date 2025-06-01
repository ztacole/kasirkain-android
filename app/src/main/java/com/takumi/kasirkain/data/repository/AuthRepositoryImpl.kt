package com.takumi.kasirkain.data.repository

import com.takumi.kasirkain.data.local.LocalDataSource
import com.takumi.kasirkain.data.remote.RemoteDataSource
import com.takumi.kasirkain.domain.model.User
import com.takumi.kasirkain.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
): AuthRepository {
    override suspend fun login(username: String, password: String): String {
        val token =  remote.login(username, password)
        local.saveToken(token)
        return token
    }

    override suspend fun profile(): User {
        val response = remote.profile()
        return response.toDomain()
    }

    override suspend fun getToken(): String? {
        return local.getToken()
    }

    override suspend fun logout() {
        remote.logout()
        local.clearToken()
    }
}