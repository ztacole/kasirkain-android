package com.takumi.kasirkain.presentation.features.scan.components

import android.graphics.Rect
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@Composable
fun BarcodeScannerCamera(
    modifier: Modifier = Modifier,
    onBarcodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var scanned by remember { mutableStateOf(false) }

    val scanAreaWidthPercent = 0.85f  // Wider scan area
    val scanAreaHeightPercent = 0.25f // Thinner scan area for linear barcodes

    Box(
        modifier.fillMaxSize()
    ) {
        // Camera Preview
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val barcodeScanner = BarcodeScanning.getClient(
                        BarcodeScannerOptions.Builder()
                            .setBarcodeFormats(
                                Barcode.FORMAT_CODE_128,
                                Barcode.FORMAT_CODE_39,
                                Barcode.FORMAT_CODE_93,
                                Barcode.FORMAT_CODABAR,
                                Barcode.FORMAT_EAN_13,
                                Barcode.FORMAT_EAN_8,
                                Barcode.FORMAT_ITF,
                                Barcode.FORMAT_UPC_A,
                                Barcode.FORMAT_UPC_E
                            )
                            .build()
                    )

                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                                val mediaImage = imageProxy.image

                                if (mediaImage != null && !scanned) {
                                    val image = InputImage.fromMediaImage(
                                        mediaImage,
                                        imageProxy.imageInfo.rotationDegrees
                                    )

                                    val imageWidth = mediaImage.width
                                    val imageHeight = mediaImage.height

                                    val scanRectWidth = imageWidth * scanAreaWidthPercent
                                    val scanRectHeight = imageHeight * scanAreaHeightPercent
                                    val scanRectLeft = (imageWidth - scanRectWidth) / 2
                                    val scanRectTop = (imageHeight - scanRectHeight) / 2 + 70

                                    val scanRect = Rect(
                                        scanRectLeft.toInt(),
                                        scanRectTop.toInt(),
                                        (scanRectLeft + scanRectWidth).toInt(),
                                        (scanRectTop + scanRectHeight).toInt()
                                    )

                                    barcodeScanner.process(image)
                                        .addOnSuccessListener { barcodes ->
                                            for (barcode in barcodes) {
                                                barcode.boundingBox?.let { barcodeBox ->
                                                    if (scanRect.contains(barcodeBox)) {
                                                        barcode.rawValue?.let {
                                                            scanned = true
                                                            cameraProvider.unbindAll()
                                                            onBarcodeScanned(it)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        .addOnFailureListener {
                                            Log.e("BarcodeScanner", "Error: ${it.message}")
                                        }
                                        .addOnCompleteListener {
                                            imageProxy.close()
                                        }
                                } else imageProxy.close()
                            }
                        }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageAnalyzer
                        )
                    } catch (e: Exception) {
                        Log.e("BarcodeCamera", "Camera binding failed", e)
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            }
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val scanRectWidth = canvasWidth * 0.85f
            val scanRectHeight = canvasHeight * 0.25f
            val scanRectLeft = (canvasWidth - scanRectWidth) / 2
            val scanRectTop = (canvasHeight - scanRectHeight) / 2

            drawRect(
                color = Color.Black.copy(alpha = 0.6f),
                size = size
            )

            drawRect(
                color = Color.Transparent,
                topLeft = Offset(scanRectLeft, scanRectTop),
                size = Size(scanRectWidth, scanRectHeight),
                blendMode = BlendMode.Clear
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val rectWidth = 300f * 0.85f
            val rectHeight = 300f * 0.25f

            Box(
                modifier = Modifier
                    .width(rectWidth.dp)
                    .height(rectHeight.dp)
                    .align(Alignment.Center)
            ) {
                // Top left corner
                Canvas(modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopStart)) {
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 3f
                    )
                }

                // Top right corner
                Canvas(modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopEnd)) {
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 3f
                    )
                }

                // Bottom left corner
                Canvas(modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.BottomStart)) {
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 3f
                    )
                }

                // Bottom right corner
                Canvas(modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.BottomEnd)) {
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 3f
                    )
                }

                // Scan line animation
                val infiniteTransition = rememberInfiniteTransition(label = "scan_line")
                val yPosition by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = rectHeight * 2,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "scan_line_y"
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawLine(
                        color = Color.Red,
                        start = Offset(0f, yPosition),
                        end = Offset(size.width, yPosition),
                        strokeWidth = 2f
                    )
                }
            }
        }
    }
}