package com.takumi.kasirkain.presentation.features.cart

import android.Manifest
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.takumi.kasirkain.domain.model.Category
import com.takumi.kasirkain.domain.model.Product
import com.takumi.kasirkain.presentation.common.components.ErrorDialog
import com.takumi.kasirkain.presentation.features.main.home.HomeViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel()
) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.BLUETOOTH_CONNECT)

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    if (permissionState.status.isGranted) {

    } else {
        ErrorDialog(
            message = "Izin Bluetooth diperlukan"
        ) { }
    }

    Button(
        onClick = {
            if (permissionState.status.isGranted) {
                viewModel.printReceipt(
                    listOf(
                        Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),Product(
                            id = 1,
                            name = "Baju Jean",
                            category = Category(1, "Baju"),
                            price = 20000,
                            image = "",
                            variantCount = 1
                        ),
                    ),
                    "RPP02N"
                )
            }
        }
    ) {
        Text("Print")
    }
}
