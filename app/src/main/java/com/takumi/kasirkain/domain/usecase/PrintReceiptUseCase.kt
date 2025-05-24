import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.RequiresPermission
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.takumi.kasirkain.R
import com.takumi.kasirkain.domain.model.CartItem
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PrintReceiptUseCase @Inject constructor() {
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    operator fun invoke(data: List<CartItem>, printerName: String, context: Context) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) return
        if (!bluetoothAdapter.isEnabled) return

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices

        try {
            pairedDevices?.forEach { device ->
                if (device.name?.contains(printerName) == true ||
                    (printerName.isEmpty() && device.name?.contains("RPP02N") == true)) {

                    val bluetoothConnection = BluetoothConnection(device)

                    val printer = EscPosPrinter(
                        bluetoothConnection,
                        203, // DPI
                        48f, // Character width
                        32   // Number of characters per line
                    )
                    try {
                        val drawable = context.resources.getDrawableForDensity(
                            R.drawable.fashion24_logo_b_w,
                            DisplayMetrics.DENSITY_MEDIUM
                        )
                        val logoHex = PrinterTextParserImg.bitmapToHexadecimalString(printer, drawable)
                        printer.printFormattedText("[C]<img>$logoHex</img>\n")
                    } catch (e: Exception) {
                        printer.printFormattedText("[C]<font size='big'><b>KASIR KAIN</b></font>\n")
                        printer.printFormattedText("[C]Fashion24\n")
                        e.printStackTrace()
                    }
                    val receiptText = generateReceiptText(data)
                    printer.printFormattedText(receiptText)
                    printer.printFormattedText("\n\n\n")

                    return
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateReceiptText(data: List<CartItem>): String {
        val sb = StringBuilder()

        sb.append("[L]\n")
        sb.append("[C]<u><font size='big'>RECEIPT</font></u>\n")
        sb.append("[L]\n")
        sb.append("[C]================================\n")
        sb.append("[L]\n")

        var totalPrice = 0.0
        data.forEach { item ->
            val itemTotal = item.productPrice * item.quantity
            totalPrice += itemTotal

            sb.append("[L]<b>${item.productName}</b>[R]${formatCurrency(itemTotal.toDouble())}\n")
            sb.append("[L]  Qty: ${item.quantity} x ${formatCurrency(item.productPrice.toDouble())}\n")
            sb.append("[L]  Barcode: ${item.barcode}\n")
            sb.append("[L]\n")
        }

        sb.append("[C]--------------------------------\n")

        sb.append("[R]<b>TOTAL :[R]${formatCurrency(totalPrice)}</b>\n")
        sb.append("[L]\n")
        sb.append("[C]================================\n")
        sb.append("[L]\n")

        sb.append("[C]Terima kasih telah berbelanja!\n")
        sb.append("[L]\n")

        val timestamp = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        sb.append("[C]${timestamp}\n")
        sb.append("[L]\n")

        return sb.toString()
    }

    private fun formatCurrency(amount: Double): String {
        return String.format("Rp %.0f", amount)
    }
}