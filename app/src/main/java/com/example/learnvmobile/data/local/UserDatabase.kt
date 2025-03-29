package com.example.learnvmobile.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.learnvmobile.domain.model.Course
import com.example.learnvmobile.domain.model.Lesson
import com.example.learnvmobile.domain.model.User
import com.example.learnvmobile.domain.model.UserProgress
import com.example.learnvmobile.utils.Converter

@Database(entities = [User::class, Lesson::class, Course::class, UserProgress::class],
    version = 4,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun lessonDao(): LessonDao
    abstract fun courseDao(): CourseDao




}