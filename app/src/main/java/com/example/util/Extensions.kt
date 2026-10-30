package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import java.text.NumberFormat
import java.util.Locale

/**
 * Currency & Rupee Extensions for TrendGram
 */
fun Double.toInr(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return format.format(this).replace("INR", "₹").trim()
}

fun Double.formatRupees(): String {
    return "₹${String.format(Locale.getDefault(), "%.2f", this)}"
}

/**
 * Number & Views Count Formatter (e.g. 1.4M, 240K)
 */
fun Long.toCompactCount(): String {
    return when {
        this >= 1_000_000 -> String.format(Locale.getDefault(), "%.1fM", this / 1_000_000.0)
        this >= 1_000 -> String.format(Locale.getDefault(), "%.1fK", this / 1_000.0)
        else -> this.toString()
    }
}

fun Int.toCompactCount(): String = this.toLong().toCompactCount()

/**
 * Timer Formatter (e.g. 60s -> "01:00", 45s -> "00:45")
 */
fun Int.formatSeconds(): String {
    val mins = this / 60
    val secs = this % 60
    return String.format(Locale.getDefault(), "%02d:%02d", mins, secs)
}

/**
 * Android Context Extensions
 */
fun Context.showToast(message: String, isLong: Boolean = false) {
    Toast.makeText(this, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun Context.shareAppOrVideo(title: String, shareUrl: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, "Watch '$title' and earn rewards on TrendGram: $shareUrl")
    }
    this.startActivity(Intent.createChooser(intent, "Share TrendGram"))
}

fun Context.openUpiPayment(upiId: String, name: String, amount: Double, note: String = "TrendGram Payout") {
    try {
        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("am", String.format(Locale.US, "%.2f", amount))
            .appendQueryParameter("cu", "INR")
            .appendQueryParameter("tn", note)
            .build()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        this.startActivity(Intent.createChooser(intent, "Pay via UPI"))
    } catch (_: Exception) {
        showToast("No UPI App found on device")
    }
}

/**
 * Jetpack Compose Modifier Extensions
 */
fun Modifier.bounceClick(
    scaleDown: Float = 0.94f,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = spring(),
        label = "bounceScale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            clip = false
        }
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
}
