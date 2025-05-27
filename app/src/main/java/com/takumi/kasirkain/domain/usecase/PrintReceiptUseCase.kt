package com.takumi.kasirkain.domain.usecase

import android.Manifest
import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.RequiresPermission
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.takumi.kasirkain.R
import com.takumi.kasirkain.domain.model.CartItem
import com.takumi.kasirkain.domain.model.TransactionHeader
import com.takumi.kasirkain.domain.model.User
import com.takumi.kasirkain.domain.repository.AuthRepository
import com.takumi.kasirkain.domain.repository.CartRepository
import com.takumi.kasirkain.domain.repository.TransactionRepository
import com.takumi.kasirkain.presentation.util.CoreFunction
import com.takumi.kasirkain.presentation.util.PrinterManager
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PrintReceiptUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository
) {
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    suspend operator fun invoke(
        printer: PrinterManager.BluetoothPrinter?,
        paymentType: String,
        cashReceived: Long,
        context: Context,
        transactionId: Int = -1
    ) {
        val cartItems = cartRepository.getCartItems()
        val item = transactionRepository.getTransactionById(transactionId)
        val cashierProfile = authRepository.profile()

        try {
            printer?.let { selectedPrinter ->
                val bluetoothConnection = BluetoothConnection(selectedPrinter.device)

                val escPosPrinter = EscPosPrinter(
                    bluetoothConnection,
                    203, // DPI
                    48f, // Character width
                    32   // Number of characters per line
                )

                // Print konten struk
                val receiptText = if (transactionId == -1) {
                    generateReceiptTextFromCart(cartItems, paymentType, cashReceived, cashierProfile)
                } else {
                    generateReceiptTextFromHistory(item, paymentType, cashReceived, cashierProfile)
                }

                // Print logo atau header
                try {
                    val drawable = context.resources.getDrawableForDensity(
                        R.drawable.fashion24_logo,
                        DisplayMetrics.DENSITY_MEDIUM
                    )
                    val logoHex = PrinterTextParserImg.bitmapToHexadecimalString(escPosPrinter, drawable)
                    escPosPrinter.printFormattedText("[C]<img>$logoHex</img>\n")
                } catch (e: Exception) {
                    escPosPrinter.printFormattedText("[C]<font size='big'><b>Fashion 24</b></font>\n")
                    e.printStackTrace()
                }
                escPosPrinter
                    .printFormattedText(
                        "[L]\n" +
                                "[C]SMK Negeri 24 Jakarta\n" +
                                formatAddress("Jl. Bambu Hitam No.3, RT.3/RW.1, Bambu Apus, Kec. Cipayung, Kota Jakarta Timur, Daerah Khusus Ibukota Jakarta 13890") +
                                receiptText
                )
            } ?: throw Exception("Printer tidak dipilih")
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e.message ?: "Gagal mencetak struk")
        }
    }

    private fun generateReceiptTextFromCart(
        data: List<CartItem>,
        paymentType: String,
        cashReceived: Long,
        cashierProfile: User
    ): String {
        val sb = StringBuilder()
        val totalPrice = data.sumOf { it.productPrice * it.quantity }.toLong()
        val timestamp = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        sb.append("[C]================================\n")
        sb.append("[L]Kasir : ${cashierProfile.username}\n")
        sb.append("[C]================================\n")
        sb.append("[L]\n")

        data.forEach { item ->
            sb.append(formatReceiptItem(
                name = item.productName,
                color = item.productColor,
                size = item.productSize,
                price = item.productPrice,
                quantity = item.quantity
            ))
        }

        sb.append("[L]\n")
        sb.append("[C]--------------------------------\n")
        sb.append("[L]<b>Total Item (${data.size}) :[R]${CoreFunction.rupiahFormatter(totalPrice)}</b>\n")
        sb.append("[L]Total Disc :[R]${CoreFunction.rupiahFormatter(0)}\n")
        sb.append("[L]Total Harga :[R]${CoreFunction.rupiahFormatter(totalPrice)}\n")
        sb.append("[L]$paymentType :[R]${CoreFunction.rupiahFormatter(cashReceived)}\n")
        sb.append("[L]Kembalian :[R]${CoreFunction.rupiahFormatter(cashReceived - totalPrice)}\n")
        sb.append("[C]================================\n")
        sb.append("[L]\n")
        sb.append("[C]Terima kasih telah berbelanja!\n")
        sb.append("[C]${timestamp}\n")
        sb.append("[L]\n")

        return sb.toString()
    }

    private fun generateReceiptTextFromHistory(
        data: TransactionHeader,
        paymentType: String,
        cashReceived: Long,
        cashierProfile: User
    ): String {
        val sb = StringBuilder()
        val totalPrice = data.details.sumOf { it.product.finalPrice * it.quantity }.toLong()
        val timestamp = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        sb.append("[C]================================\n")
        sb.append("[L]Kasir : ${cashierProfile.username}\n")
        sb.append("[C]================================\n")
        sb.append("[L]\n")

        data.details.forEach { item ->
            sb.append(formatReceiptItem(
                name = item.product.name,
                color = item.product.variants[0].color,
                size = item.product.variants[0].size,
                price = item.product.finalPrice,
                quantity = item.quantity
            ))
        }

        sb.append("[L]\n")
        sb.append("[C]--------------------------------\n")
        sb.append("[L]<b>Total Item (${data.details.size}) :[R]${CoreFunction.rupiahFormatter(totalPrice)}</b>\n")
        sb.append("[L]Total Disc :[R]${CoreFunction.rupiahFormatter(0)}\n")
        sb.append("[L]Total Harga :[R]${CoreFunction.rupiahFormatter(totalPrice)}\n")
        sb.append("[L]$paymentType :[R]${CoreFunction.rupiahFormatter(cashReceived)}\n")
        sb.append("[L]Kembalian :[R]${CoreFunction.rupiahFormatter(cashReceived - totalPrice)}\n")
        sb.append("[C]================================\n")
        sb.append("[L]\n")
        sb.append("[C]Terima kasih telah berbelanja!\n")
        sb.append("[C]${timestamp}\n")
        sb.append("[L]\n")

        return sb.toString()
    }

    fun formatReceiptItem(
        name: String,
        color: String,
        size: String,
        price: Int,
        quantity: Int
    ): String {
        val totalBefore = quantity * price
        val discount = ((price * (10 / 100)) * quantity).toLong()
        val totalAfter = totalBefore.toLong() - discount

        val line1 = "[L]<b>$quantity ${name.take(13)}</b> ${CoreFunction.currencyFormatter(price.toLong())}[R]${CoreFunction.currencyFormatter(totalBefore.toLong())}\n"
        val line2 = "[L]   Size: $size | Warna: ${color.take(5)}\n"
        val line3 = if (discount > 0) {
            "[L]   Diskon:[R]${CoreFunction.currencyFormatter(discount)}\n" +
                    "[L]   Subtotal:[R]${CoreFunction.currencyFormatter(totalAfter)}\n"
        } else {
            ""
        }

        return line1 + line2 + line3
    }

    private fun formatAddress(address: String, maxLineLength: Int = 32): String {
        return if (address.length <= maxLineLength) {
            "[C]$address\n"
        } else {
            val words = address.split(" ")
            val lines = mutableListOf<String>()
            var currentLine = ""

            words.forEach { word ->
                if (currentLine.length + word.length + 1 <= maxLineLength) {
                    currentLine += "$word "
                } else {
                    lines.add(currentLine.trim())
                    currentLine = "$word "
                }
            }
            lines.add(currentLine.trim())

            lines.joinToString("\n") { "[C]$it" } + "\n"
        }
    }
}