package com.takumi.kasirkain.data.remote.response

import com.takumi.kasirkain.domain.model.Transaction
import com.takumi.kasirkain.domain.model.TransactionHeader
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class TransactionHeaderResponse(
    val id: Int,
    val user: UserResponse,
    val payment_type: String,
    val cash_received: Int,
    val change_returned: Int,
    val total: Int,
    val details: List<TransactionDetailResponse>,
    val created_at: String
) {
    fun toDomain(): TransactionHeader {
        val localeId = Locale("in", "ID")
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", localeId)
        val timeFormatter = SimpleDateFormat("dd MMM yyyy HH:mm:ss", localeId)
        timeFormatter.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val time = dateFormatter.parse(created_at)
        val timeInString = timeFormatter.format(time!!)
        return TransactionHeader(
            id = id,
            user = user.toDomain(),
            paymentType = payment_type,
            cashReceived = cash_received,
            changeReturned = change_returned,
            total = total,
            details = details.map { it.toDomain() },
            createdAt = timeInString
        )
    }
}
