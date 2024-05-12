package com.github.core.domain.user.model

import android.os.Parcelable
import com.github.core.data.user.local.entity.UserDetailEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetail(
    val username: String,
    val avatar: String? = "",
    val name: String? = "",
    val company: String? = "-",
    val location: String? = "-",
    val repository: Int? = 0,
    val followers: Int? = 0,
    val following: Int? = 0,
    val isFavorite: Boolean? = false
) : Parcelable {

    fun mapToEntity() = UserDetailEntity(
        username,
        avatar,
        name,
        company,
        location,
        repository,
        followers,
        following,
        isFavorite
    )
}

