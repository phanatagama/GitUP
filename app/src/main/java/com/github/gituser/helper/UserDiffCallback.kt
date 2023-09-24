package com.github.gituser.helper

import androidx.recyclerview.widget.DiffUtil
import com.github.gituser.domain.user.entity.UserDetailEntity

class UserDiffCallback(private val mOldUserList: List<UserDetailEntity>, private val mNewUserList: List<UserDetailEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldUserList.size
    }

    override fun getNewListSize(): Int {
        return mNewUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserList[oldItemPosition].username == mNewUserList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldUserList[oldItemPosition]
        val newEmployee = mNewUserList[newItemPosition]
        return oldEmployee.username == newEmployee.username && oldEmployee.avatar == newEmployee.avatar
    }
}