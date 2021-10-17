package com.github.gituser.ui.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.github.gituser.database.User
import com.github.gituser.repository.UserRepository

class UserAddUpdateViewModel(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)
    fun insert(user: User) = mUserRepository.insert(user)
    fun delete(user: User) = mUserRepository.delete(user)
}