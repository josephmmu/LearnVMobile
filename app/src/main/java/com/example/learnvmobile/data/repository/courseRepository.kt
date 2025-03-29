package com.example.learnvmobile.data.repository

import com.example.learnvmobile.data.local.CourseDao
import com.example.learnvmobile.data.local.UserProgressDao
import com.example.learnvmobile.domain.model.Course
import com.example.learnvmobile.domain.model.UserProgress
import javax.inject.Inject

class courseRepository @Inject constructor(

    private val courseDao: CourseDao
) {

    suspend fun getCourse(courseId: String) = courseDao.getCourse(courseId)

    suspend fun insertCourse(course: Course) = courseDao.insertCourse(course)

    suspend fun getAllCourses() = courseDao.getAllCourses()
}
