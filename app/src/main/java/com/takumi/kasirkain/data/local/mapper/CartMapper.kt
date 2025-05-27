package com.takumi.kasirkain.data.local.mapper

import com.takumi.kasirkain.data.local.entity.CartEntity
import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.domain.model.ProductVariant

fun Product.toCartItem(
    productVariant: ProductVariant,
    quantity: Int = 1
): CartItem {
    return CartItem(
        id = 0,
        productId = id,
        productName = name,
        productImage = image,
        productPrice = price,
        productDiscount = discount,
        productFinalPrice = finalPrice,
        productEventName = if (activeEvents.isNotEmpty()) activeEvents[0].name else null,
        productEventStartDate = if (activeEvents.isNotEmpty()) activeEvents[0].startDate else null,
        productEventEndDate = if (activeEvents.isNotEmpty()) activeEvents[0].endDate else null,
        productVariantId = productVariant.id,
        productSize = productVariant.size,
        productColor = productVariant.color,
        barcode = productVariant.barcode,
        stock = productVariant.stock,
        quantity = quantity
    )
}

fun CartEntity.toDomain(): CartItem {
    return CartItem(
        id = id,
        productId = productId,
        productName = productName,
        productImage = productImage,
        productPrice = productPrice,
        productDiscount = productDiscount,
        productFinalPrice = productFinalPrice,
        productEventName = productEventName,
        productEventStartDate = productEventStartDate,
        productEventEndDate = productEventEndDate,
        productVariantId = productVariantId,
        productSize = productSize,
        productColor = productColor,
        barcode = barcode,
        stock = stock,
        quantity = quantity
    )
}

fun CartItem.toData(): CartEntity {
    return CartEntity(
        productId = productId,
        productName = productName,
        productImage = productImage,
        productPrice = productPrice,
        productDiscount = productDiscount,
        productFinalPrice = productFinalPrice,
        productEventName = productEventName,
        productEventStartDate = productEventStartDate,
        productEventEndDate = productEventEndDate,
        productVariantId = productVariantId,
        productSize = productSize,
        productColor = productColor,
        barcode = barcode,
        stock = stock,
        quantity = quantity
    )
}