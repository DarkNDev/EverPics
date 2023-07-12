package com.darkndev.everpics.recyclerview.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.darkndev.everpics.R
import com.darkndev.everpics.databinding.LayoutUnsplashItemBinding
import com.darkndev.everpics.models.UnsplashPhoto

class UnsplashPhotoViewHolder(
    private val binding: LayoutUnsplashItemBinding,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(bindingAdapterPosition)
            }
        }
    }

    fun bind(photo: UnsplashPhoto) {
        binding.apply {
            Glide.with(itemView)
                .load(photo.urls.regular)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.error)
                .into(imageView)

            textView.text = photo.user.name
        }
    }
}