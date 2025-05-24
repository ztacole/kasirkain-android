package com.takumi.kasirkain.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import java.text.NumberFormat
import java.util.Locale
import androidx.core.graphics.createBitmap

object CoreFunction {
    fun formatToRupiah(number: Int): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number)
    }

//    fun renderComposableToBitmapAsync(
//        context: Context,
//        width: Int,
//        height: Int,
//        content: @Composable () -> Unit,
//        onRendered: (Bitmap) -> Unit
//    ) {
//        val composeView = ComposeView(context).apply {
//            setContent { content() }
//            layoutParams = ViewGroup.LayoutParams(width, height)
//        }
//
//        val root = FrameLayout(context).apply {
//            addView(composeView)
//        }
//
//        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val layoutParams = WindowManager.LayoutParams().apply {
//            this.width = 1
//            this.height = 1
//            type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
//            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//            format = PixelFormat.TRANSLUCENT
//        }
//
//        // Tambahkan ke Window
//        windowManager.addView(root, layoutParams)
//
//        composeView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                composeView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//
//                composeView.measure(
//                    View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
//                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)
//                )
//                composeView.layout(0, 0, width, composeView.measuredHeight)
//
//                val bitmap = Bitmap.createBitmap(
//                    composeView.width,
//                    composeView.height,
//                    Bitmap.Config.ARGB_8888
//                )
//                val canvas = Canvas(bitmap)
//                composeView.draw(canvas)
//
//                windowManager.removeViewImmediate(root)
//                onRendered(bitmap)
//            }
//        })
//    }

}