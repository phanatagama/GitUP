package com.github.gituser.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity( indices = [Index(value = ["username"], unique = true)])
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "username")
    var username: String? = "",

    @ColumnInfo(name = "avatar")
    var avatar: String? = "",

    @ColumnInfo(name = "name")
    var name: String? = "",

    @ColumnInfo(name ="company")
    var company: String? = "",

    @ColumnInfo(name = "location")
    var location: String? = "",

    @ColumnInfo(name = "repository")
    var repository: Int? = 0,

    @ColumnInfo(name = "followers")
    var followers: Int? = 0,

    @ColumnInfo(name = "following")
    var following: Int? = 0
) : Parcelable
