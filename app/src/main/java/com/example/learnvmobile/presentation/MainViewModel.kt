package com.example.learnvmobile.presentation

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnvmobile.MainActivity
import com.example.learnvmobile.data.repository.UserProgressRepository
import com.example.learnvmobile.data.repository.UserRepository
import com.example.learnvmobile.data.repository.firestoreRepository
import com.example.learnvmobile.di.Resource
import com.example.learnvmobile.domain.model.Course
import com.example.learnvmobile.domain.model.Lesson
import com.example.learnvmobile.domain.model.User
import com.example.learnvmobile.domain.model.UserProgress
import com.example.learnvmobile.firestore.FirestoreClient
import com.example.learnvmobile.google.GoogleAuthClient
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UserRepository,
    private val userProgressRepository: UserProgressRepository,
    private val firestoreRepository: firestoreRepository,
    private val googleAuthClient: GoogleAuthClient,
    private val firestoreClient: FirestoreClient
) : ViewModel() {

    init {
        viewModelScope.launch {
            googleAuthClient.signOut()
        }
    }

    var user by mutableStateOf(User(0,"","","", "", ""))
        private set

    var courses by mutableStateOf<List<Course>>(emptyList())
        private set

    var lessons by mutableStateOf<List<Lesson>>(emptyList())
        private set

    var userProgress by mutableStateOf<Map<String, Boolean>>(emptyMap())
        private set

    // For local user idk, Im using it for room
    private val _user = MutableStateFlow<User?>(null)
    val userStateFlow: StateFlow<User?> = _user.asStateFlow()

    // Trying something else for googleSignIn
    fun signIn(activity: Activity, onResult: (User) -> Unit, onUserExists: () -> Unit) {
        viewModelScope.launch {
            val userFromFirebase = googleAuthClient.signIn(activity)
            
            if (userFromFirebase != null){
                val email = userFromFirebase.email ?: ""

                val existingUser = repository.getUserByEmail(email)


                if (existingUser != null) {
                    println("User already exists in Room: ${existingUser.id}")
                    this@MainViewModel.user = existingUser
                    onResult(existingUser)
                } else {
                    val user1 = User(
                        id = 0,
                        fullName = userFromFirebase.displayName ?: "",
                        email = email,
                        password = "",
                        courseID = "",
                        authProvider = "Google",
                        createdAt = Date()
                    )

                    val insertedId = repository.insertUser(user1).toInt()

                    val insertedUser = repository.getUserById(insertedId)

                    if (insertedUser != null) {
                        println("New user inserted: ${insertedUser.fullName}, ID: ${insertedUser.id}")
                        this@MainViewModel.user = insertedUser
                        onResult(insertedUser)
                    }

//                    // The thing below should replace that
//                    insertUserIfNotExists(
//                        user = user1,
//                        onUserExists = {
//                            Toast.makeText(activity, "User already exists!", Toast.LENGTH_SHORT).show()
//                            this@MainViewModel.user = user1
//                            onResult(user1)
//                        },
//                        onUserInserted = {insertedId ->
//                            println("User inserted successfully with ID: $insertedId")
//                            this@MainViewModel.user = user1
//                            onResult(user1)
//                        }
//                    )

                }





            } else {
                onUserExists()
            }
        }
    }
    private var deleteUsers: User? = null

    fun insertUserIfNotExists(user: User, onUserExists: () -> Unit, onUserInserted: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingUser = repository.getUserByEmail(user.email)
            if (existingUser == null) {

                //Insert to room first
                val insertedId = repository.insertUser(user)
                val userWithId = user.copy(id = insertedId.toInt())


                // Now insert to firebase
                firestoreClient.insertUser(userWithId).collect { firestoreId ->
                    getUserById(userWithId.id)
                    //_user.value = userWithId
                    // Switch to main thread before calling UI-related actions
                    withContext(Dispatchers.Main) {
                        onUserInserted(insertedId)
                    }
                }
            } else {
                withContext(Dispatchers.Main) {onUserExists()}
            }
        }
    }

    fun checkUserExists(email : String, onUserExists: (User) -> Unit, onUserNotFound: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUserByEmail(email)
            withContext(Dispatchers.Main) {
                if (user != null) {
                    onUserExists(user)
                } else {
                    onUserNotFound()
                }
            }
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUsers()
        }
    }

    fun getUserById(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val userFromDb = repository.getUserById(id)
            Log.d("MainViewModel", "Querying user with ID: $id")  // Debug
            Log.d("MainViewModel", "Fetched user: $userFromDb") // Debugging
            user = userFromDb
        }
    }

    fun getUserById2(id : Int): User {
        return runBlocking {
            repository.getUserById(id)
        }
    }

    // For Courses and Lessons and User Progress

    fun loadCourses() {
        viewModelScope.launch {
            courses = listOf(
                Course("math101", "Math Basics", "Learn the basics of math."),
                Course("sci101", "Science Basics", "Explore the fundamentals of science.")
            )
        }
    }

    fun loadLessons(courseId: String) {
        viewModelScope.launch {
            lessons = listOf(
                Lesson("lesson1", courseId, "Introduction", "Welcome to this course.", 0),
                Lesson("lesson2", courseId, "Chapter 1", "Understanding numbers.", 1)
            )
        }
    }

    fun loadUserProgress(userId: Int, courseId: String) {
        viewModelScope.launch {
            val localProgress = userProgressRepository.getUserProgress(userId, courseId)
            firestoreRepository.getUserProgress(userId.toString(), courseId) { remoteProgress ->
                if (remoteProgress != null) {
                    viewModelScope.launch {
                        userProgressRepository.insertProgress(remoteProgress)
                    }
                }
                userProgress = localProgress.associate { it.currentLessonId to it.isCompleted }
            }

        }
    }

    fun markLessonCompleted(userId: Int, courseId: String, lessonId: String) {
        viewModelScope.launch {
            val newProgress = UserProgress(
                id = 0,
                userId = userId,
                courseId = courseId,
                currentLessonId = lessonId,
                isCompleted = true,
                progressPercentage = 100f
            )
            userProgressRepository.insertProgress(newProgress)
            firestoreRepository.saveUserProgress(userId.toString(), newProgress)
            userProgress = userProgress + (lessonId to true)
        }
    }

    // Not being used but should keep

    var getAllUsers = repository.getAllUsers()

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user = user)

            _user.value = user
        }
    }

    fun checkRoomUserToFirebase(email : String, onUserExists: (User) -> Unit, onUserNotFound: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUserByEmail(email)
            withContext(Dispatchers.Main) {
                if (user != null) {
                    onUserExists(user)
                } else {
                    onUserNotFound()
                }
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user =  user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUsers = user
            repository.deleteUser(user =  user)
        }
    }

    fun undoDeletedUser() {
        deleteUsers?.let { user ->
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertUser(user = user)
            }
        }
    }

    suspend fun getUserByStateFlowId(id : Int) : User? {
        return repository.getUserById(id)
    }

    fun getUserByEmail(email : String)  {
        viewModelScope.launch(Dispatchers.IO) {
            user = repository.getUserByEmail(email = email)!!

        }
    }

    fun updateUserEmailPassword(email: String, password: String) {
        user = user.copy(email = email, password = password)
    }

    fun updateName(fullName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            user = user.copy(fullName = fullName)
        }
    }



}