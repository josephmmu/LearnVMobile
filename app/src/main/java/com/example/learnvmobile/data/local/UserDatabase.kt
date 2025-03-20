package com.example.learnvmobile.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.learnvmobile.domain.model.User
import com.example.learnvmobile.utils.Converter

@Database(entities = [User::class], version = 1, exportSchema = true)
@TypeConverters(Converter::class)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

}