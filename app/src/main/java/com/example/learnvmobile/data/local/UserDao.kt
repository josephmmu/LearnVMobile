package com.example.learnvmobile.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.learnvmobile.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user : User) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User)

    @Query("UPDATE User SET fullName = :fullName WHERE id = :id")
    suspend fun updateUserName(id: Int, fullName: String)

    @Query("SELECT email FROM User WHERE email = :email")
    suspend fun checkUser(email: String) : String

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM User")
    suspend fun deleteDallUsers()

    @Query("SELECT * FROM User WHERE id =:id")
    suspend fun getUserById(id: Int): User

    @Query("SELECT * FROM User where email = :email")
    suspend fun getUserByEmail(email : String): User?

    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>


}