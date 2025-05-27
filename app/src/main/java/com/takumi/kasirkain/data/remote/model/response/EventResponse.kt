package com.takumi.kasirkain.data.remote.model.response

import com.takumi.kasirkain.domain.model.Event

data class EventResponse(
    val id: Int,
    val name: String,
    val description: String,
    val start_date: String,
    val end_date: String,
) {
    fun toDomain() : Event{
        return Event(
            id = id,
            name = name,
            description = description,
            startDate = start_date,
            endDate = end_date
        )
    }
}
