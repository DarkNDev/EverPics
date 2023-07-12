package com.darkndev.everpics.recyclerview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.darkndev.everpics.databinding.LayoutUnsplashItemBinding
import com.darkndev.everpics.models.UnsplashPhoto

class UnsplashPagingAdapter(private val onItemClick: (UnsplashPhoto) -> Unit) :
    PagingDataAdapter<UnsplashPhoto, UnsplashPhotoViewHolder>(
        UnsplashPhotoDiffUtil
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnsplashPhotoViewHolder {
        return UnsplashPhotoViewHolder(
            LayoutUnsplashItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = {
                val item = getItem(it)
                if (item != null) {
                    onItemClick(item)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: UnsplashPhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
}