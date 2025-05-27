package com.takumi.kasirkain.presentation.util

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.annotation.RequiresPermission

class PrinterManager(private val context: Context) {
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun getAvailablePrinters(): List<BluetoothPrinter> {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() ?: return emptyList()
        if (!bluetoothAdapter.isEnabled) return emptyList()

        return bluetoothAdapter.bondedDevices.map { device ->
            BluetoothPrinter(
                name = device.name ?: "Unknown",
                address = device.address,
                device = device
            )
        }
    }

    data class BluetoothPrinter(
        val name: String,
        val address: String,
        val device: BluetoothDevice
    )
}