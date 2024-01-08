package com.github.core.data.user.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.core.domain.user.model.UserDetail

@Entity(tableName = "user", indices = [Index(value = ["username"], unique = true)])
data class UserDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "avatar")
    var avatar: String? = "",

    @ColumnInfo(name = "name")
    var name: String? = "",

    @ColumnInfo(name ="company")
    var company: String? = "-",

    @ColumnInfo(name = "location")
    var location: String? = "-",

    @ColumnInfo(name = "repository")
    var repository: Int? = 0,

    @ColumnInfo(name = "followers")
    var followers: Int? = 0,

    @ColumnInfo(name = "following")
    var following: Int? = 0,

    @ColumnInfo(name = "favorite")
    var isFavorite: Boolean? = false
)

fun UserDetailEntity.mapToDomain() : UserDetail{
    return UserDetail(username,avatar, name, company, location, repository, followers, following, isFavorite)
}