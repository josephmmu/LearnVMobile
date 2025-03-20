package com.example.learnvmobile.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String ="",
    val email: String ="",
    val password: String = "",
    val courseID: String = "",
    val authProvider: String, // "manual" or "google"
    val createdAt: Date = Date()
)