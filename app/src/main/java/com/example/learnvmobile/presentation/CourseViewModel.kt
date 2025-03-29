//package com.example.learnvmobile.presentation
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.learnvmobile.data.repository.LessonRepository
//import com.example.learnvmobile.data.repository.courseRepository
//import com.example.learnvmobile.data.repository.firestoreRepository
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//class CourseViewModel @Inject constructor(
//    private val courseRepository: courseRepository,
//    private val lessonRepository : LessonRepository,
//    private val firestoreRepository: firestoreRepository
//) : ViewModel() {
//
////    fun syncCourses() {
////        firestoreRepository.getCourses { courses ->
////            viewModelScope.launch {
////                courses.forEach { courseRepository.insertCourse(it) }
////            }
////        }
////    }
//
////    fun syncLessons(courseId: String) {
////        firestoreRepository.getLessons(courseId) { lessons ->
////            viewModelScope.launch {
////                lessons.forEach { lessonRepository.insertLesson(it)}
////            }
////        }
////    }
//
//}