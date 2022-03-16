package com.khairo.windowmanagerwithfodablescreen.data.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.window.layout.DisplayFeature
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

fun View.snack(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    snack(resources.getString(messageRes), length, f)
}

fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

suspend fun String.suspendToast(context: Context?, duration: Int = Toast.LENGTH_SHORT) {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, this@suspendToast, duration).show()
    }
}

fun String.toast(context: Context?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, duration).apply { show() }
}


fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}


fun String.log() {
    Timber.d(this)
}

fun View.getFeatureBoundsInWindow(
    displayFeature: DisplayFeature,
    includePadding: Boolean = true
): Rect? {
    // Adjust the location of the view in the window to be in the same coordinate space as the feature.
    val viewLocationInWindow = IntArray(2)
    getLocationInWindow(viewLocationInWindow)

    // Intersect the feature rectangle in window with view rectangle to clip the bounds.
    val viewRect = Rect(
        viewLocationInWindow[0], viewLocationInWindow[1],
        viewLocationInWindow[0] + width, viewLocationInWindow[1] + height
    )

    // Include padding if needed
    if (includePadding) {
        viewRect.left += paddingLeft
        viewRect.top += paddingTop
        viewRect.right -= paddingRight
        viewRect.bottom -= paddingBottom
    }

    val featureRectInView = Rect(displayFeature.bounds)
    val intersects = featureRectInView.intersect(viewRect)

    // Checks to see if the display feature overlaps with our view at all
    if ((featureRectInView.width() == 0 && featureRectInView.height() == 0) ||
        !intersects
    ) {
        return null
    }

    // Offset the feature coordinates to view coordinate space start point
    featureRectInView.offset(-viewLocationInWindow[0], -viewLocationInWindow[1])

    return featureRectInView
}