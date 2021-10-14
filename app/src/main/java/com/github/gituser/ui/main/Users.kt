package com.github.gituser.ui.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val username: String?,
    val avatar: String?
) : Parcelable
