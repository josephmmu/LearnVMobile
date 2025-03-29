package com.example.learnvmobile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.learnvmobile.domain.model.UserProgress

@Dao
interface UserProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserProgress)

    @Query("SELECT * FROM UserProgress WHERE userId = :userId AND courseId = :courseId")
    suspend fun getUserProgress(userId: Int, courseId: String): List<UserProgress>

    @Update
    suspend fun updateProgress(progress: UserProgress)

}