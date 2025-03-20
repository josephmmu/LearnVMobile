package com.example.learnvmobile.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.learnvmobile.data.local.UserDao
import com.example.learnvmobile.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao : UserDao,
    private val auth: FirebaseAuth
) {

    suspend fun insertUser(user : User): Long = dao.insertUser(user = user)

    suspend fun updateUser(user : User) = dao.updateUser(user = user)

    suspend fun updateUserName(user: User) =
        user.fullName?.let { dao.updateUserName(id = user.id, fullName = it) }

    suspend fun deleteUser(user : User) = dao.deleteUser(user = user)

    suspend fun deleteAllUsers() = dao.deleteDallUsers()

    suspend fun getUserById(id: Int): User = dao.getUserById(id = id)

    suspend fun getUserByEmail(email : String) = dao.getUserByEmail(email = email)

    //Obsolete ?
    //suspend fun isThereEmailDuplicate(email: String) : Boolean = dao.getUserByEmail(email = email) != null

    fun getAllUsers(): Flow<List<User>> = dao.getAllUsers()

    // Authenticate with Google and save user in Room
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loginWithGoogle(idToken: String): User? {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user

            firebaseUser?.let {
                val user = User(
                    id = it.uid.toInt(),
                    email = it.email ?: "generic@email.com",
                    password = "",
                    courseID = "",
                    fullName = it.displayName ?: "Name",
                    createdAt = Date(),
                    authProvider = "Google"
                )
                dao.insertUser(user)
                user
            } 

        } catch (e: Exception) {
            null
        }
    } // end of loginWithGoogle

    //Firebase Logout
    suspend fun logout() {
        auth.signOut()
    }
}
