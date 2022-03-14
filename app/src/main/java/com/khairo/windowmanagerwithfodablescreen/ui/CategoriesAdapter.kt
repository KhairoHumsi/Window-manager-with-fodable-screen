package com.khairo.windowmanagerwithfodablescreen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khairo.windowmanagerwithfodablescreen.data.models.CategoriesModel
import com.khairo.windowmanagerwithfodablescreen.databinding.CategoryItemBinding

//class CategoriesAdapter(private val listener: OnClickListener) :
//    BaseAdapter<CategoriesModel, BaseViewHolder<CategoriesModel>>() {
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): BaseViewHolder<CategoriesModel> =
//        ViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//
//    override fun onBindViewHolder(holder: BaseViewHolder<CategoriesModel>, position: Int) {
//        when (holder) {
//            is ViewHolder -> holder.bind(getRow(position), position)
//
//            else -> throw IllegalArgumentException()
//        }
//    }
//
//    inner class ViewHolder(private var binding: CategoryItemBinding) :
//        ViewHolders(binding = binding) {
//        override fun bind(model: CategoriesModel, position: Int) {
//            binding.let {
//                it.model = model
//                it.listener = listener
//                it.executePendingBindings()
//            }
//        }
//    }
//
//    interface OnClickListener {
//        fun loadItems(model: CategoriesModel)
//    }
//}


class CategoriesAdapter(
    private val listener: OnClickListener
) : ListAdapter<CategoriesModel, CategoriesAdapter.ViewHolder>(CategoriesDiffCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CategoriesModel) {
            binding.let {
                it.model = model
                it.listener = listener
                it.executePendingBindings()
            }
        }
    }

    interface OnClickListener {
        fun loadItems(model: CategoriesModel)
    }
}

object CategoriesDiffCallBack : DiffUtil.ItemCallback<CategoriesModel>() {
    override fun areItemsTheSame(oldItem: CategoriesModel, newItem: CategoriesModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CategoriesModel, newItem: CategoriesModel): Boolean =
        oldItem == newItem
}