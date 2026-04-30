package com.misw.vinilos.ui.collector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.misw.vinilos.data.model.Collector
import com.misw.vinilos.databinding.ItemCollectorBinding

class CollectorAdapter : ListAdapter<Collector, CollectorAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCollectorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemCollectorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(collector: Collector) {
            binding.tvCollectorName.text = collector.name
            binding.tvCollectorEmail.text = collector.email
            
            // Load a generated circular avatar based on the collector's ID
            val avatarUrl = "https://i.pravatar.cc/150?u=${collector.id}"
            
            com.bumptech.glide.Glide.with(binding.root.context)
                .load(avatarUrl)
                .transform(com.bumptech.glide.load.resource.bitmap.CircleCrop())
                .into(binding.ivCollectorAvatar)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Collector>() {
        override fun areItemsTheSame(oldItem: Collector, newItem: Collector) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Collector, newItem: Collector) = oldItem == newItem
    }
}
