package com.example.learnvmobile.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey


@Entity(
    tableName = "Lesson",
    foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = ["courseId"],
        childColumns = ["courseId"],
        onDelete = CASCADE
    )]
)
data class Lesson(
    @PrimaryKey val lessonId: String,
    val courseId: String,
    val title: String,
    val content: String,
    @ColumnInfo(name = "order", defaultValue = "0")
    val order: Int // <-- Make sure this is defined if you're using it in queries
)
