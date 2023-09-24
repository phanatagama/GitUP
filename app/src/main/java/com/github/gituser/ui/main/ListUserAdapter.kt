package com.github.gituser.ui.main

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.gituser.databinding.ItemRowUserBinding
import com.github.gituser.domain.user.entity.UserEntity

class ListUserAdapter(private val listUser: MutableList<UserEntity>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(data: List<UserEntity>){
        listUser.clear()
        listUser.addAll(data)
        Log.d(TAG, "updateList: $listUser")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity)
    }

    inner class ListViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: UserEntity){
            Log.d(TAG, "bind: BINDING DATA INTO VIEW STARTED")
            with(binding){
            Glide.with(itemView.context)
                .load(data.avatar)
                .apply(RequestOptions().override(55, 55))
                .into(imgItemPhoto)

            tvItemName.text = data.username
            tvItemUsername.text = data.username
            root.setOnClickListener {
                onItemClickCallback.onItemClicked(data)
            }
            }
        }
    }
    companion object {
        const val TAG ="LIST_USER_ADAPTER_TAG"
    }
}