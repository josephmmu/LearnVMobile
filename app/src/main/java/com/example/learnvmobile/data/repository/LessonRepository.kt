package com.example.learnvmobile.data.repository

import com.example.learnvmobile.data.local.LessonDao
import com.example.learnvmobile.domain.model.Lesson
import javax.inject.Inject

class LessonRepository @Inject constructor(
    private val lessonDao: LessonDao
) {

    suspend fun getLessonByCourse(courseId: String) = lessonDao.getLessonByCourse(courseId)

    suspend fun getLessonById(lessonId: String) = lessonDao.getLessonById(lessonId)

    suspend fun insertLesson(lesson: Lesson) = lessonDao.insertLesson(lesson)
}