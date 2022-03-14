package com.khairo.windowmanagerwithfodablescreen.data.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.khairo.windowmanagerwithfodablescreen.common.App
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
