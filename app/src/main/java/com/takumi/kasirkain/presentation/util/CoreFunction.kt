package com.takumi.kasirkain.presentation.util

import java.text.NumberFormat
import java.util.Locale

object CoreFunction {
    fun formatToRupiah(number: Int): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number)
    }
}