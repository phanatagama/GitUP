package com.github.core.domain.user.model

import android.os.Parcelable
import com.github.core.data.user.local.entity.UserDetailEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetail(
    var username: String,
    var avatar: String? = "",
    var name: String? = "",
    var company: String? = "-",
    var location: String? = "-",
    var repository: Int? = 0,
    var followers: Int? = 0,
    var following: Int? = 0,
    var isFavorite: Boolean? = false
) : Parcelable

fun UserDetail.mapToEntity() = UserDetailEntity(username, avatar, name, company, location, repository, followers, following, isFavorite)