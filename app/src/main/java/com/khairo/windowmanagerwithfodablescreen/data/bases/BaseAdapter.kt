package com.khairo.windowmanagerwithfodablescreen.data.bases

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import java.util.*

abstract class BaseAdapter<T : Any, B : BaseViewHolder<T>> :
    ListAdapter<T, B>(COMPARATOR<T>().diffUtil) {

    private var items = Collections.emptyList<T>()

    fun setItems(list: List<T>) {
        items = list
    }

    fun getItems(): List<T> = items
    fun getRow(position: Int): T = items[position]
    fun removeRow(position: Int) {
        items.removeAt(position)
    }

    fun addRow(position: Int, model: T) {
        items.add(position, model)
    }

    fun addRow(model: T) {
        items.add(model)
    }

    fun updateRow(position: Int, model: T) {
        items[position] = model
    }

    override fun getItemCount(): Int = items.size

    abstract inner class ViewHolders(binding: ViewDataBinding) : BaseViewHolder<T>(binding.root)
}

class COMPARATOR<T> {
    val diffUtil = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }
}
