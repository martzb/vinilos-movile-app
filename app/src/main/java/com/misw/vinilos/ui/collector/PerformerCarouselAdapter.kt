package com.misw.vinilos.ui.collector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.misw.vinilos.R
import com.misw.vinilos.data.model.Performer
import com.misw.vinilos.databinding.ItemPerformerCarouselBinding

class PerformerCarouselAdapter(
    private val onItemClick: ((Performer) -> Unit)? = null
) : ListAdapter<Performer, PerformerCarouselAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPerformerCarouselBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemPerformerCarouselBinding,
        private val onItemClick: ((Performer) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(performer: Performer) {
            binding.tvPerformerName.text = performer.name
            Glide.with(binding.ivPerformerPhoto.context)
                .load(performer.image)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .transform(CircleCrop())
                .into(binding.ivPerformerPhoto)

            binding.root.setOnClickListener { onItemClick?.invoke(performer) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Performer>() {
        override fun areItemsTheSame(oldItem: Performer, newItem: Performer) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Performer, newItem: Performer) = oldItem == newItem
    }
}
