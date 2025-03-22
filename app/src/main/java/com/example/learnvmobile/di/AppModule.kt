package com.example.learnvmobile.di

import android.content.Context
import androidx.room.Room
import com.example.learnvmobile.data.local.UserDao
import com.example.learnvmobile.data.local.UserDatabase
import com.example.learnvmobile.data.repository.UserRepository
import com.example.learnvmobile.google.GoogleAuthClient
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
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db : UserDatabase): UserDao = db.userDao()

    
    @Provides
    @Singleton
    fun provideUserRepository(dao : UserDao): UserRepository = UserRepository(dao = dao)

    @Provides
    @Singleton
    fun provideGoogleAuthClient(@ApplicationContext context: Context): GoogleAuthClient {
        return GoogleAuthClient(context)
    }

}