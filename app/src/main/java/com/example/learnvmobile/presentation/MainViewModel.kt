package com.example.learnvmobile.presentation

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnvmobile.MainActivity
import com.example.learnvmobile.data.repository.UserRepository
import com.example.learnvmobile.di.Resource
import com.example.learnvmobile.domain.model.User
import com.example.learnvmobile.google.GoogleAuthClient
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UserRepository,
    private val googleAuthClient: GoogleAuthClient,
) : ViewModel() {

    init {
        viewModelScope.launch {
            googleAuthClient.signOut()
        }
    }

    var user by mutableStateOf(User(0,"","","", "", ""))
        private set

    // For local user idk, Im using it for room
    private val _user = MutableStateFlow<User?>(null)
    val userStateFlow: StateFlow<User?> = _user.asStateFlow()

    // For firebase
    private val _userState = MutableStateFlow<Resource<User>>(Resource.Loading())
    val userState: StateFlow<Resource<User>> = _userState

    // for google Signin??
    private val _loginState = mutableStateOf<Boolean?>(null)
    val loginState: State<Boolean?> = _loginState

    // Trying something else for googleSignIn
    fun signIn(activity: Activity, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val userFromFirebase = googleAuthClient.signIn(activity)
            
            if (userFromFirebase != null){
                val user = User(
                    id = 0,
                    fullName = userFromFirebase.displayName ?: "",
                    email = userFromFirebase.email ?:"",
                    password = "",
                    courseID = "",
                    authProvider = "Google",
                    createdAt = Date()
                )

                repository.insertUser(user)
                onResult(true)
            } else {
                onResult(false)
            }
            


        }
    }

    var getAllUsers = repository.getAllUsers()

    private var deleteUsers: User? = null

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user = user)
            _user.value = user
        }
    }

    fun insertUserIfNotExists(user: User, onUserExists: () -> Unit, onUserInserted: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingUser = repository.getUserByEmail(user.email)
            if (existingUser == null) {
                val insertedId: Long = repository.insertUser(user)
                //repository.insertUser(user = user)
                _user.value = user
                withContext(Dispatchers.Main) {onUserInserted(insertedId)}
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

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUsers()
        }
    }

    fun undoDeletedUser() {
        deleteUsers?.let { user ->
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertUser(user = user)
            }
        }
    }

    fun getUserById(id : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            user = repository.getUserById(id = id)
        }
    }

    suspend fun getUserByStateFlowId(id : Int) : User? {
        return repository.getUserById(id)
    }

    fun getUserByEmail(email : String) {
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