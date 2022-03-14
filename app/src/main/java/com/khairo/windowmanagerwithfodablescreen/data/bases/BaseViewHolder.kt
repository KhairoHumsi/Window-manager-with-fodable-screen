package com.khairo.windowmanagerwithfodablescreen.data.bases

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(model: T, position: Int)
}
