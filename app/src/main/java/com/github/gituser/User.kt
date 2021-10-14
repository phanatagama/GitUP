package com.github.gituser

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "username")
    val username: String?,

//    val name: String?,
    @ColumnInfo(name = "avatar")
    val avatar: String?
//    val company: String?,
//    val location: String?,
//    val repository: Int?,
//    val follower: Int?,
//    val following: Int?
)
