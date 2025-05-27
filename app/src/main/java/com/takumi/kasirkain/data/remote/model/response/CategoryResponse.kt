package com.takumi.kasirkain.data.remote.model.response

import com.takumi.kasirkain.domain.model.Category

data class CategoryResponse(
    val id: Int,
    val name: String
) {
    fun toDomain(): Category {
        return Category(
            id = id,
            name = name
        )
    }
}
