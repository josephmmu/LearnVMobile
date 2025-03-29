package com.example.learnvmobile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnvmobile.domain.model.Course

@Dao
interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Query("SELECT * FROM Course WHERE courseId = :courseId")
    suspend fun getCourse(courseId: String): Course

    @Query("SELECT * FROM Course")
    suspend fun getAllCourses(): List<Course>


}