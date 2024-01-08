package com.github.core.domain.user.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String?,
    val avatar: String?
) : Parcelable
