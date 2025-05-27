package com.takumi.kasirkain.domain.model

data class Event(
    val id: Int,
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String,
)