package com.segel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey
    val id: Int,
    val name: String,
    val designation: String,
    val number: String,
    val email: String,
    val password: String,
    val image: String)
