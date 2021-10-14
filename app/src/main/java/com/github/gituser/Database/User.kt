package com.github.gituser.Database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "username")
    var username: String? = null,

//    val name: String?,
    @ColumnInfo(name = "avatar")
    var avatar: String? = null
//    val company: String?,
//    val location: String?,
//    val repository: Int?,
//    val follower: Int?,
//    val following: Int?
) : Parcelable
