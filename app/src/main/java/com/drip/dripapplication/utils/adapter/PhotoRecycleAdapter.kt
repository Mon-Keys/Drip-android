package com.drip.dripapplication.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drip.dripapplication.R
import com.drip.dripapplication.databinding.PhotoItemBinding

class PhotoRecycleAdapter : RecyclerView.Adapter<PhotoRecycleAdapter.ViewHolder>() {

    var userPhoto: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val itemBinding: PhotoItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(photo: String){
            //Glide
            Glide.with(itemView)
                //.load("https://drip.monkeys.team/$photo")
                .load(photo)
                .placeholder(R.drawable.icon_baseline_image)
                .error(R.drawable.icon_baseline_broken_image)
                .into(itemBinding.profilePhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = userPhoto[position]
        holder.bind(photo)
    }

    override fun getItemCount() = userPhoto.size

}