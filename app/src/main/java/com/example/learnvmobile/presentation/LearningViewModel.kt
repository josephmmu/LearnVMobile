//package com.example.learnvmobile.presentation
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.learnvmobile.data.repository.UserProgressRepository
//import com.example.learnvmobile.data.repository.firestoreRepository
//import com.example.learnvmobile.domain.model.UserProgress
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class LearningViewModel @Inject constructor(
//    private val userProgressRepository: UserProgressRepository,
//    private val firestoreRepository: firestoreRepository
//) : ViewModel() {
//
//    fun syncUserProgress(userId: Int, courseId: String) {
//        viewModelScope.launch {
//            val localProgress = userProgressRepository.getUserProgress(userId = userId, courseId = courseId)
//
//            firestoreRepository.getUserProgress(userId.toString(), courseId) { remoteProgress ->
//                if (remoteProgress != null) {
//                    // Sync local with Firestore if there's new progress
//                    viewModelScope.launch {
//                        userProgressRepository.insertProgress(remoteProgress)
//                    }
//                }
//            }
//
//        }
//    } // end of Sync User Progress
//
//    fun updateUserProgress(progress: UserProgress) {
//        viewModelScope.launch {
//            userProgressRepository.updateProgress(progress)
//            firestoreRepository.saveUserProgress(progress.userId.toString(), progress)
//        }
//    }
//
//}