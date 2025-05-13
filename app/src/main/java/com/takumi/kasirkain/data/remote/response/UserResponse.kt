package com.takumi.kasirkain.data.remote.response

import com.takumi.kasirkain.domain.model.User

data class UserResponse(
    val id: Int,
    val username: String
) {
    fun toDomain(): User {
        return User(
            id = id,
            username = username
        )
    }
}
