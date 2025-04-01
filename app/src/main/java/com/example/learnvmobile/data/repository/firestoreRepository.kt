package com.example.learnvmobile.data.repository

import android.util.Log
import com.example.learnvmobile.domain.model.Course
import com.example.learnvmobile.domain.model.Lesson
import com.example.learnvmobile.domain.model.UserProgress
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import javax.inject.Inject

class firestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
){
    fun saveUserProgress(userId:String, progress: UserProgress) {
        val data = hashMapOf(
            "userId" to progress.userId,
            "courseId" to progress.courseId,
            "currentLessonId" to progress.currentLessonId,
            "isCompleted" to progress.isCompleted,
            "progressPercentage" to progress.progressPercentage
        )

        firestore.collection("users")
            .document(userId)
            .collection("progress")
            .document(progress.courseId)
            .set(data)
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saving progress", e)
            }


    }

    fun getUserProgress(userId: String, courseId: String, onComplete: (UserProgress?) -> Unit) {
        firestore.collection("users")
            .document(userId)
            .collection("progress")
            .document(courseId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val progress = UserProgress(
                        id = 0,
                        userId = document.getLong("userId")?.toInt() ?:0,
                        courseId = document.getString("courseId") ?: "",
                        currentLessonId = document.getString("currentLesson") ?: "",
                        isCompleted = document.getBoolean("isCompleted") ?: false,
                        progressPercentage = document.getDouble("progressPercentage")?.toFloat() ?: 0f
                     )
                    onComplete(progress)
                } else {
                    onComplete(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching progress", e)
                onComplete(null)
            }
    }
}