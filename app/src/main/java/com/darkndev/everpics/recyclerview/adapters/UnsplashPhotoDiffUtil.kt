package com.darkndev.everpics.recyclerview.adapters

import androidx.recyclerview.widget.DiffUtil
import com.darkndev.everpics.models.UnsplashPhoto

object UnsplashPhotoDiffUtil : DiffUtil.ItemCallback<UnsplashPhoto>() {
    override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
        oldItem == newItem
}