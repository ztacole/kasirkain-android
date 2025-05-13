package com.takumi.kasirkain.data.remote.response

import com.takumi.kasirkain.domain.model.TransactionHeader
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class TransactionHeaderResponse(
    val id: Int,
    val user: UserResponse,
    val payment_type: String,
    val product_count: Int,
    val total: Int,
    val created_at: String
) {
    fun toDomain(): TransactionHeader {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeFormatter.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val time = dateFormatter.parse(created_at)
        val timeInString = timeFormatter.format(time!!)
        return TransactionHeader(
            id = id,
            user = user.toDomain(),
            paymentType = payment_type,
            productCount = product_count,
            total = total,
            time = timeInString
        )
    }
}
