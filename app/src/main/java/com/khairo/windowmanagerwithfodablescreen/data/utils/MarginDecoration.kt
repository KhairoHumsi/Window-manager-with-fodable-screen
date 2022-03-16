package com.khairo.windowmanagerwithfodablescreen.data.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.intuit.sdp.R

class MarginDecoration(context: Context) : ItemDecoration() {
    private val margin: Int = context.resources.getDimensionPixelSize(R.dimen._4sdp)
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect[margin, margin, margin] = margin
    }

}
