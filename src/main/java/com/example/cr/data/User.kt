package com.example.cr.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val profileImageUri: String? = null,
    val points: Int = 0
)


// test1@example.com / 1234
//test2@example.com / 5678