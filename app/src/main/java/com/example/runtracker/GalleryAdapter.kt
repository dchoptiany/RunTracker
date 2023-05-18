package com.example.gallery

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runtracker.Images
import com.example.runtracker.R
import com.squareup.picasso.Picasso
import java.io.File

class GalleryAdapter(var img: ArrayList<Images>, var onImgClickListener: OnImageClickListeren): RecyclerView.Adapter<GalleryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item,parent,false)
        view.layoutParams.height = parent.height / 4
        return GalleryViewHolder(view,onImgClickListener)
    }

    override fun getItemCount(): Int {
        return img.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val cw = ContextWrapper(holder.view.context)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val myImageFile = File(directory, img[position].path)

        Picasso.get().load(myImageFile ).into(holder.imgView)
    }

    interface OnImageClickListeren{
        fun onImgClick(position : Int)
    }
}