package com.example.runtracker.gallery

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.R

class GalleryViewHolder(itemView: View, private var onImageClickListener: GalleryAdapter.OnImageClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var imageView: ImageView

    init {
        imageView = itemView.findViewById(R.id.imageView2)
        imageView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onImageClickListener.onImageClick(adapterPosition)
    }
}