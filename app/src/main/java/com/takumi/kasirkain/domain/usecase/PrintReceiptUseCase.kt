package com.takumi.kasirkain.domain.usecase

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.RequiresPermission
import com.takumi.kasirkain.R
import com.takumi.kasirkain.domain.model.Product
import java.io.OutputStream
import java.util.UUID
import javax.inject.Inject

class PrintReceiptUseCase @Inject constructor(
    private val context: Context // Inject context to access image resources
) {
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    operator fun invoke(data: List<Product>, printerName: String) {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        val device = adapter?.bondedDevices?.firstOrNull { it.name == printerName }

        if (device != null) {
            val socket = device.createRfcommSocketToServiceRecord(
                UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
            )
            socket.connect()

            val output = socket.outputStream

            // 1. Initialize printer
            output.write(byteArrayOf(0x1B, 0x40)) // ESC @ - Reset printer

            // 2. Print Logo (TOP CENTER)
            printLogo(output) // Implement this function

            // 3. Set text alignment to center for header
            output.write(byteArrayOf(0x1B, 0x61, 0x01)) // ESC a 1 - Center align

            // 4. Print header text
            val builder = StringBuilder().apply {
                appendLine() // Blank line after logo
                appendLine("Kasir Kain") // Your business name
                appendLine("----------------------------")
            }

            // 5. Set back to left alignment for items
            output.write(byteArrayOf(0x1B, 0x61, 0x00)) // ESC a 0 - Left align

            // 6. Print items
            data.forEach { product ->
                builder.appendLine(product.name)
            }

            // 7. Print footer
            builder.apply {
                appendLine("----------------------------")
                appendLine("TOTAL: Rp100.000,00")
            }

            output.write(builder.toString().toByteArray())
            output.flush()
            socket.close()
        } else {
            throw Exception("Printer not found")
        }
    }

    private fun printLogo(outputStream: OutputStream) {
        try {
            // Method 1: If you have the logo as a bitmap resource
            val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.kasirkain_logo)

            // Method 2: If you have the logo as a file
            // val logoBitmap = BitmapFactory.decodeFile("/path/to/logo.png")

            // Convert bitmap to printer format
            val logoBytes = convertBitmapToEscPos(logoBitmap)
            outputStream.write(logoBytes)

        } catch (e: Exception) {
            // Fallback: Print text header if logo fails
            outputStream.write("[YOUR LOGO]\n".toByteArray())
        }
    }

    private fun convertBitmapToEscPos(bitmap: Bitmap): ByteArray {
        // This is a simplified conversion - you may need a proper library
        // for your specific printer model

        val width = bitmap.width
        val height = bitmap.height

        // ESC * command - Select bitmap mode
        val escStar = byteArrayOf(0x1B, 0x2A, 0x21) // 24-dot double density

        // Width bytes (LSB, MSB)
        val widthBytes = byteArrayOf(
            (width % 256).toByte(),
            (width / 256).toByte()
        )

        // Height bytes (LSB, MSB)
        val heightBytes = byteArrayOf(
            (height % 256).toByte(),
            (height / 256).toByte()
        )

        // Convert bitmap to 1-bit depth data
        val imageData = convertTo1Bit(bitmap)

        return escStar + widthBytes + heightBytes + imageData
    }

    private fun convertTo1Bit(bitmap: Bitmap): ByteArray {
        // Implement proper 1-bit conversion here
        // This is just a placeholder
        return ByteArray(bitmap.width * bitmap.height / 8)
    }
}