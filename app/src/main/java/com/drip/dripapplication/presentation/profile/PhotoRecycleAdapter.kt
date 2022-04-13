package com.drip.dripapplication.presentation.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.drip.dripapplication.databinding.PhotoItemBinding

class PhotoRecycleAdapter : RecyclerView.Adapter<PhotoRecycleAdapter.ViewHolder>() {

    var userPhoto: List<String> = emptyList()
        set(value) {
            val diffCallback = DiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    inner class ViewHolder(private val itemBinding: PhotoItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(photo: String){
            //Glide
            Glide.with(itemView)
                .load(photo)
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

    class DiffCallback(private val oldList: List<String>, private val newList: List<String>): DiffUtil.Callback(){

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }

}