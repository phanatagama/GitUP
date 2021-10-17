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
    var username: String? = null,

    @ColumnInfo(name = "avatar")
    var avatar: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name ="company")
    var company: String? = null,

    @ColumnInfo(name = "location")
    var location: String? = null,

    @ColumnInfo(name = "repository")
    var repository: Int? = null,

    @ColumnInfo(name = "followers")
    var followers: Int? = null,

    @ColumnInfo(name = "following")
    var following: Int? = null
) : Parcelable
