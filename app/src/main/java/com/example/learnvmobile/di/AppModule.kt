package com.example.learnvmobile.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.learnvmobile.data.local.UserDao
import com.example.learnvmobile.data.local.LessonDao
import com.example.learnvmobile.data.local.CourseDao
import com.example.learnvmobile.data.local.UserProgressDao
import com.example.learnvmobile.data.local.UserDatabase
import com.example.learnvmobile.data.repository.UserRepository
import com.example.learnvmobile.google.GoogleAuthClient
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context : Context) : UserDatabase{
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "local_db"
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3,MIGRATION_3_4)
            .build()
    }

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS Lesson (
                lessonId TEXT PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                courseId TEXT NOT NULL,
                content TEXT NOT NULL,
                "order" INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY(courseId) REFERENCES Course(courseId) ON DELETE CASCADE
            )
            """.trimIndent()
            )
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS Course (courseId TEXT NOT NULL, title TEXT NOT NULL, description TEXT NOT NULL, PRIMARY KEY(courseId))"
            )
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS UserProgress (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                userId INTEGER NOT NULL, 
                courseId TEXT NOT NULL, 
                currentLessonId TEXT NOT NULL, 
                progressPercentage REAL NOT NULL, 
                isCompleted INTEGER NOT NULL, 
                FOREIGN KEY(userId) REFERENCES User(id) ON DELETE CASCADE
            )
            """.trimIndent()
            )
        }
    }

    @Provides
    @Singleton
    fun provideUserDao(db : UserDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideLessonDao(db : UserDatabase): LessonDao = db.lessonDao()

    @Provides
    @Singleton
    fun provideCourseDao(db : UserDatabase): CourseDao = db.courseDao()

    @Provides
    @Singleton
    fun provideUserProgressDao(db : UserDatabase): UserProgressDao = db.userProgressDao()

    
    @Provides
    @Singleton
    fun provideUserRepository(dao : UserDao): UserRepository = UserRepository(dao = dao)

    @Provides
    @Singleton
    fun provideGoogleAuthClient(@ApplicationContext context: Context): GoogleAuthClient {
        return GoogleAuthClient(context)
    }



    @Provides
    @Singleton
    fun provideFirestoreRepository(userProgressDao: UserProgressDao): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }



}