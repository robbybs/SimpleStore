package com.rbs.simplestore.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rbs.simplestore.databinding.ItemCartBinding
import com.rbs.simplestore.domain.model.CartDomain

class CartAdapter : ListAdapter<CartDomain, CartAdapter.ViewHolder>(DIFF_CALLBACK) {
    private var onDeleteClickListener: ((CartDomain) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        onDeleteClickListener?.let { holder.bind(item, it) }
    }

    fun setOnDeleteClickListener(listener: (CartDomain) -> Unit) {
        onDeleteClickListener = listener
    }

    class ViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CartDomain, onItemClickListener: ((CartDomain) -> Unit)) {
            data.apply {
                binding.apply {
                    Glide.with(itemView.context)
                        .load(image)
                        .into(ivProduct)

                    val formattedPrice = "$$price"
                    tvName.text = title
                    tvPrice.text = formattedPrice

                    btnDelete.setOnClickListener {
                        onItemClickListener.invoke(data)
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartDomain>() {
            override fun areItemsTheSame(oldItem: CartDomain, newItem: CartDomain): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CartDomain, newItem: CartDomain): Boolean =
                oldItem == newItem
        }
    }
}