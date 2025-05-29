package com.takumi.kasirkain.domain.usecase

import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.domain.repository.CartRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(): List<CartItem> {
        val cartItems = repository.getCartItems()

        return cartItems.map { item ->
            if (item.productEventName != null &&
                item.productEventStartDate != null &&
                item.productEventEndDate != null
            ) {
                try {
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val startDate = formatter.parse(item.productEventStartDate)
                    val endDate = formatter.parse(item.productEventEndDate)
                    val currentDate = Calendar.getInstance().time

                    val calendar = Calendar.getInstance()
                    calendar.time = endDate!!
                    calendar.add(Calendar.DATE, 1)
                    val endOfDay = calendar.time

                    if (currentDate.before(startDate) || currentDate.after(endOfDay)) {
                        item.copy(
                            productEventName = null,
                            productEventStartDate = null,
                            productEventEndDate = null,
                            productDiscount = 0,
                            productFinalPrice = item.productPrice
                        )
                    } else {
                        item
                    }
                } catch (e: Exception) {
                    item
                }
            } else {
                item
            }
        }
    }
}