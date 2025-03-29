package com.example.learnvmobile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnvmobile.domain.model.Lesson

@Dao
interface LessonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: Lesson)

    @Query("SELECT * FROM Lesson WHERE courseId = :courseId ORDER BY `order` ASC")
    suspend fun getLessonByCourse(courseId: String): List<Lesson>

    @Query("SELECT * FROM Lesson WHERE lessonId = :lessonId")
    suspend fun getLessonById(lessonId: String): Lesson?

}