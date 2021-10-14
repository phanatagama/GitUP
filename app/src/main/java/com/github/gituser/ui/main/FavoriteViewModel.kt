package com.github.gituser.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.gituser.Database.User
import com.github.gituser.repository.UserRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)
    fun getAllUsers(): LiveData<List<User>> = mUserRepository.getAllUsers()
}