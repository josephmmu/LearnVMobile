package com.example.learnvmobile.firestore

import com.example.learnvmobile.domain.model.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreClient @Inject constructor(){

    private val tag = "Firestore Client: "

    private val db = FirebaseFirestore.getInstance()
    private val collection = "users"


    fun insertUser(
        user: User
    ): Flow<String?> {
    return callbackFlow {
            db.collection(collection)
                .add(user.ToHashMap())
                .addOnSuccessListener {document ->
                    println(tag + "Insert user with id: ${document.id}")

                    CoroutineScope(Dispatchers.IO).launch {
                        updateUser(user).collect{}
                    }
                    trySend(document.id)
                }
                .addOnFailureListener {e ->
                    e.printStackTrace()
                    println(tag + "error inserting user: ${e.message}")
                    trySend(null)
                }
            awaitClose{}
        }
    }

    fun updateUser(
        user: User
    ): Flow<Boolean> {
        return callbackFlow {
            db.collection(collection)
                .document(user.id.toString())
                .set(user.ToHashMap())
                .addOnSuccessListener {
                    println(tag + "updated user with id: ${user.id}")
                    trySend(true)
                }
                .addOnFailureListener {e ->
                    e.printStackTrace()
                    println(tag + "error updating user: ${e.message}")
                    trySend(false)
                }
            awaitClose{}
        }
    }

    fun getUser(
        email: String
    ): Flow<User?> {
        return callbackFlow {
            db.collection(collection)
                .get()
                .addOnSuccessListener {result ->
                    var user: User? = null

                    for (document in result) {
                        if (document.data["email"] == email ) {
                            user = document.data.toUser()
                            println(tag + "User found with: ${user.email}")
                            trySend(user)

                        }
                    }

                    if (user == null) {

                        println(tag + "User not found with: $email")
                        trySend(null)
                    }
                }
                .addOnFailureListener {e ->
                    e.printStackTrace()
                    println(tag + "error getting user: ${e.message}")
                    trySend(null)
                }
            awaitClose{}
        }
    }

    private fun User.ToHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "fullName" to fullName,
            "email" to email,
            "password" to password,
            "courseID"  to courseID,
            "authProvider" to authProvider,
            "createdAt" to createdAt
        )
    }

    private fun Map<String, Any>.toUser(): User {
        return User(
            id = (this["id"] as? Long)?.toInt() ?: 0,
            fullName = this["fullName"] as String,
            email = this["email"] as String,
            password = this["password"] as String,
            courseID = this["courseID"] as String,
            authProvider = this["authProvider"] as String,
            createdAt = (this["createdAt"] as? Timestamp)?.toDate() ?: Date()
        )
    }

}