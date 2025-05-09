package com.vanluong.search.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vanluong.model.Photo
import com.vanluong.model.calculateScaledSize
import com.vanluong.search.databinding.PhotoItemBinding


/**
 * Created by van.luong
 * on 07,May,2025
 *
 */
class SearchPhotoAdapter(val clickListener: (Photo, View) -> Unit) :
    PagingDataAdapter<Photo, SearchPhotoAdapter.SearchPhotoViewHolder>(SearchPhotoDiffCallback()) {

    override fun onBindViewHolder(holder: SearchPhotoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPhotoViewHolder {
        val binding = PhotoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchPhotoViewHolder(binding)
    }

    inner class SearchPhotoViewHolder(private val binding: PhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) = with(binding) {
            val screenWidth = binding.root.context.resources.displayMetrics.widthPixels
            val columnCount = 2
            // We have to decrease the resolution of the image to avoid OOM but still keep same aspect ratio
            val targetSize = photo.calculateScaledSize(screenWidth, columnCount)

            binding.ivPhoto.layoutParams.apply {
                width = targetSize.width
                height = targetSize.height
            }

            binding.ivPhoto.transitionName = "shared_image_${photo.id}"

            Glide.with(root.context).load(photo.url)
                .override(targetSize.width, targetSize.height)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .into(binding.ivPhoto)

            root.setOnClickListener {
                clickListener(photo, binding.ivPhoto)
            }
        }
    }
}

class SearchPhotoDiffCallback : DiffUtil.ItemCallback<Photo>() {

    override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
        oldItem.id == newItem.id
}
