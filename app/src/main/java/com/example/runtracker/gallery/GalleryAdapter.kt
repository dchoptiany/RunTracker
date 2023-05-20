package com.example.runtracker.gallery

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.R
import com.squareup.picasso.Picasso
import java.io.File

class GalleryAdapter(private var images: ArrayList<Images>, private var onImageClickListener: OnImageClickListener): RecyclerView.Adapter<GalleryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        view.layoutParams.height = parent.height / 4
        return GalleryViewHolder(view, onImageClickListener)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val cw = ContextWrapper(holder.itemView.context)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val myImageFile = File(directory, images[position].path)

        Picasso.get().load(myImageFile).into(holder.imageView)
    }

    interface OnImageClickListener {
        fun onImageClick(position: Int)
    }
}