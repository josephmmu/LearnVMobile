package com.example.learnvmobile.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    //tableName = "user_progress",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = CASCADE
    )]
)
data class UserProgress(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "userId")
    val userId: Int = 0,  // Ensure this matches User entity ID type
    @ColumnInfo(name = "courseId")
    val courseId: String = "",
    @ColumnInfo(name = "currentLessonId")
    val currentLessonId: String,
    @ColumnInfo(name = "isCompleted")
    val isCompleted: Boolean = false,
    @ColumnInfo(name = "progressPercentage")
    val progressPercentage: Float= 0f
) {
}