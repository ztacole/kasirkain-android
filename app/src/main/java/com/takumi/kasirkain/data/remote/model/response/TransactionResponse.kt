package com.takumi.kasirkain.data.remote.model.response

import com.takumi.kasirkain.domain.model.Transaction
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class TransactionResponse(
    val id: Int,
    val user: UserResponse,
    val payment_type: String,
    val cash_received: Int,
    val change_returned: Int,
    val product_count: Int,
    val total: Int,
    val created_at: String
) {
    fun toDomain(): Transaction {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeFormatter.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val time = dateFormatter.parse(created_at)
        val timeInString = timeFormatter.format(time!!)
        return Transaction(
            id = id,
            user = user.toDomain(),
            paymentType = payment_type,
            cashReceived = cash_received,
            changeReturned = change_returned,
            productCount = product_count,
            total = total,
            time = timeInString
        )
    }
}
