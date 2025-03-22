package com.example.learnvmobile.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.learnvmobile.data.local.UserDao
import com.example.learnvmobile.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao : UserDao
) {

    suspend fun insertUser(user : User): Long = dao.insertUser(user = user)

    suspend fun updateUser(user : User) = dao.updateUser(user = user)

    suspend fun updateUserName(user: User) =
        user.fullName?.let { dao.updateUserName(id = user.id, fullName = it) }

    suspend fun deleteUser(user : User) = dao.deleteUser(user = user)

    suspend fun deleteAllUsers() = dao.deleteDallUsers()

    suspend fun getUserById(id: Int): User = dao.getUserById(id = id)

    suspend fun getUserByEmail(email : String) = dao.getUserByEmail(email = email)

    fun getAllUsers(): Flow<List<User>> = dao.getAllUsers()


    suspend fun insertOrUpdateUser(user: User) {
        dao.insertUser(user)
    }
}
