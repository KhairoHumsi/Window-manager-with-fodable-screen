package com.khairo.windowmanagerwithfodablescreen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khairo.windowmanagerwithfodablescreen.data.models.CartModel
import com.khairo.windowmanagerwithfodablescreen.databinding.CartItemBinding

class CartAdapter(
    private val listener: OnClickListener
) :
    ListAdapter<CartModel, CartAdapter.ViewHolder>(CartDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private var binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CartModel) {
            binding.let {
                it.model = model
                it.listener = listener
                it.executePendingBindings()

                it.minus.setOnClickListener {
                    listener.decreaseItem(model.itemId, adapterPosition)
                }

                it.plus.setOnClickListener {
                    listener.increaseItem(model.itemId, adapterPosition)
                }

                it.delete.setOnClickListener {
                    listener.removeItem(model, adapterPosition)
                }

                it.deleteButton.setOnClickListener {
                    listener.removeItem(model, adapterPosition)
                }
            }
        }
    }

    interface OnClickListener {
        fun removeItem(model: CartModel, position: Int)
        fun decreaseItem(itemId: Int, position: Int)
        fun increaseItem(itemId: Int, position: Int)
    }
}

object CartDiffCallBack : DiffUtil.ItemCallback<CartModel>() {
    override fun areItemsTheSame(oldItem: CartModel, newItem: CartModel): Boolean =
        oldItem.itemId == newItem.itemId && oldItem.count == newItem.count

    override fun areContentsTheSame(oldItem: CartModel, newItem: CartModel): Boolean =
        oldItem == newItem
}
