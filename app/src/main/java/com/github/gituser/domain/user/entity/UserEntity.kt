package com.github.gituser.domain.user.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    val username: String?,
    val avatar: String?
) : Parcelable
