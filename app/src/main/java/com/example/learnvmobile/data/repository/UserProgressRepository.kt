package com.example.learnvmobile.data.repository

import com.example.learnvmobile.data.local.UserProgressDao
import com.example.learnvmobile.domain.model.UserProgress
import javax.inject.Inject

class UserProgressRepository @Inject constructor(
    private val progressDao: UserProgressDao
) {

    suspend fun getUserProgress(userId: Int, courseId: String) = progressDao.getUserProgress(userId, courseId)

    suspend fun insertProgress(userProgress: UserProgress) = progressDao.insertProgress(userProgress)

    suspend fun updateProgress(userProgress: UserProgress) = progressDao.updateProgress(userProgress)
}