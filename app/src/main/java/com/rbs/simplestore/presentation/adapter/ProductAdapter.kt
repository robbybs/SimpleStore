package com.rbs.simplestore.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rbs.simplestore.databinding.ItemProductBinding
import com.rbs.simplestore.domain.model.ProductDomain

class ProductAdapter : ListAdapter<ProductDomain, ProductAdapter.ViewHolder>(DIFF_CALLBACK) {
    private var onItemClickListener: ((ProductDomain) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        onItemClickListener?.let { holder.bind(item, it) }
    }

    fun setOnItemClickListener(listener: (ProductDomain) -> Unit) {
        onItemClickListener = listener
    }

    class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductDomain, onItemClickListener: ((ProductDomain) -> Unit)) {
            data.apply {
                binding.apply {
                    Glide.with(itemView.context)
                        .load(image)
                        .into(ivProduct)

                    val formattedPrice = "$$price"
                    tvTitle.text = title
                    tvPrice.text = formattedPrice
                    tvDescription.text = description

                    root.setOnClickListener { onItemClickListener.invoke(data) }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductDomain>() {
            override fun areItemsTheSame(oldItem: ProductDomain, newItem: ProductDomain): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ProductDomain, newItem: ProductDomain): Boolean =
                oldItem == newItem
        }
    }
}