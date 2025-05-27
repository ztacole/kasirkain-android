package com.takumi.kasirkain.presentation.util

import java.text.NumberFormat
import java.util.Locale
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.TimeZone

object CoreFunction {
    fun rupiahFormatter(number: Long): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number)
    }

    fun currencyFormatter(number: Long): String {
        val formatter = DecimalFormat("#,##0", DecimalFormatSymbols().apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        })

        return formatter.format(number)
    }
}