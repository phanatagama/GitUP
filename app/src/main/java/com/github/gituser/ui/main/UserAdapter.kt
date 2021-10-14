package com.github.gituser.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.gituser.Database.User
import com.github.gituser.databinding.ItemUserBinding
import com.github.gituser.helper.UserDiffCallback

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listUsers = ArrayList<User>()
    fun setListUsers(listUsers: List<User>) {
        val diffCallback = UserDiffCallback(this.listUsers, listUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUsers.clear()
        this.listUsers.addAll(listUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: UserAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return UserViewHolder(binding)
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val (id, username, avatar) = listUsers[position]
        Glide.with(holder.itemView.context)
            .load(avatar)
            .apply(RequestOptions().override(55, 55))
            .into(holder.binding.imgItemPhoto)
        holder.binding.tvItemName.text = username.toString()
        holder.binding.tvItemUsername.text = username.toString()
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUsers[holder.adapterPosition])
        }
    }
    override fun getItemCount(): Int {
        return listUsers.size
    }
    inner class UserViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(user: User) {
//            with(binding) {
//                tvItemTitle.text = user.username
//                tvItemDate.text = user.date
//                tvItemDescription.text = user.description
//                cvItemNote.setOnClickListener {
//                    val intent = Intent(it.context, UserAddUpdateActivity::class.java)
//                    intent.putExtra(UserAddUpdateActivity.EXTRA_NOTE, user)
//                    it.context.startActivity(intent)
//                }
//            }
//        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}