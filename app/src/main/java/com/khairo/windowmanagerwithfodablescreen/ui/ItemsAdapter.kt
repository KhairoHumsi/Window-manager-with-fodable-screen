package com.khairo.windowmanagerwithfodablescreen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khairo.windowmanagerwithfodablescreen.data.models.ItemModel
import com.khairo.windowmanagerwithfodablescreen.databinding.ItemsItemBinding

//class ItemsAdapter(private val listener: OnClickListener) :
//    BaseAdapter<ItemModel, BaseViewHolder<ItemModel>>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ItemModel> =
//        ViewHolder(ItemsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//
//    override fun onBindViewHolder(holder: BaseViewHolder<ItemModel>, position: Int) {
//
//        when (holder) {
//            is ViewHolder -> holder.bind(getRow(position), position)
//
//            else -> throw IllegalArgumentException()
//        }
//    }
//
//    inner class ViewHolder(private var binding: ItemsItemBinding) : ViewHolders(binding = binding) {
//        override fun bind(model: ItemModel, position: Int) {
//            binding.let {
//                it.model = model
//                it.listener = listener
//                it.executePendingBindings()
//            }
//        }
//    }
//
//    interface OnClickListener {
//        fun addItemToCart(model: ItemModel)
//    }
//}

class ItemsAdapter(
    private val listener: OnClickListener
) : ListAdapter<ItemModel, ItemsAdapter.ViewHolder>(ItemDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ItemModel) {
            binding.let {
                it.model = model
                it.listener = listener
                it.executePendingBindings()
            }
        }
    }

    interface OnClickListener {
        fun addItemToCart(model: ItemModel)
    }
}

class ItemDiffCallBack : DiffUtil.ItemCallback<ItemModel>() {
    override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        return oldItem == newItem
    }
}