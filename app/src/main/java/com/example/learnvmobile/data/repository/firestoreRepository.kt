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
        val docRef = firestore.collection("users")
            .document(userId)
            .collection("progress")
            .document(progress.courseId)


        docRef.set(progress)


//        db.collection("users").document(userId)
//            .collection("courses").document(progress.courseId)
//            .set(progress)
//            .addOnSuccessListener { Log.d("FirestoreUserProgress", "Progress Saved!") }
//            .addOnFailureListener { Log.e("FirestoreUserProgress", "Error Saving Progress", it)}

    }

    fun getUserProgress(userId: String, courseId: String, onComplete: (UserProgress?) -> Unit) {
        val docRef = firestore.collection("users")
            .document(userId)
            .collection("progress")
            .document(courseId)

        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                onComplete(document.toObject(UserProgress::class.java))
            } else {
                onComplete(null)
            }
        }

//        db.collection("users").document(userId)
//            .get()
//            .addOnSuccessListener { document ->
//                if (document.exists()) {
//                    val progress = document.toObject(UserProgress::class.java)
//                    onResult(progress)
//                } else {
//                    onResult(null)
//                }
//            }
//            .addOnFailureListener { onResult(null) }
    }

//    fun getCourses(onResult: (List<Course>) -> Unit) {
//        db.collection("courses")
//            .get()
//            .addOnSuccessListener { result ->
//                val courses = result.documents.mapNotNull { it.toObject(Course::class.java) }
//                onResult(courses)
//            }
//    }
//
//    fun getLessons(courseId: String, onResult: (List<Lesson>) -> Unit) {
//        db.collection("courses").document(courseId)
//            .collection("lessons")
//            .get()
//            .addOnSuccessListener { result ->
//                val lessons = result.documents.mapNotNull { it.toObject(Lesson::class.java) }
//                onResult(lessons)
//            }
//    }


}