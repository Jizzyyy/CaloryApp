package com.example.caloryapp.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloryapp.model.UserModel
import com.example.caloryapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    data object Loading : LoginState()
    data class Success(val user: UserModel) : LoginState()
    data class Error(val message: String) : LoginState()
}

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _registerState = MutableStateFlow<Boolean?>(null)
    val registerState: StateFlow<Boolean?> = _registerState

    // Menyimpan data login state (loading, success, error)
    private val _loginstate = mutableStateOf<LoginState>(LoginState.Loading)
    val loginstate = _loginstate

    // Menyimpan data pengguna yang login
    private val _user = mutableStateOf<UserModel?>(null)
    val user = _user

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginstate.value = LoginState.Loading
            // Fetch user by username from Firestore
            repository.getUserByUsername(username) { fetchedUser ->
                if (fetchedUser != null && fetchedUser.password == password) {
                    _user.value = fetchedUser // Store user data in ViewModel
                    _loginstate.value = LoginState.Success(fetchedUser)
                    Log.d("Login", "Fetched user: $fetchedUser")
                    Log.d("Login", "User fullName: ${fetchedUser?.fullName}")
                    Log.d("Login", "User username: ${fetchedUser?.username}")

                } else {
                    _loginstate.value = LoginState.Error("Invalid username or password")
                }
            }
        }
    }

    // **REGISTER USER**
//    fun register(email: String, password: String, user: UserModel) {
//        viewModelScope.launch {
//            val result = repository.registerUser(email, password, user)
//            _registerState.value = result
//        }
//    }

//    fun registerUser(
//        username: String,
//        fullName: String,
//        email: String,
//        password: String,
//        gender: String,
//        weight: String,
//        height: String,
//        onSuccess: () -> Unit,
//        onFailure: () -> Unit
//    ) {
//        val user = UserModel(username, fullName, email, password, gender, weight, height)
//
//        viewModelScope.launch {
//            val isSuccess = repository.registerUser(user)
//            if (isSuccess) {
//                onSuccess()
//            } else {
//                onFailure()
//            }
//        }
//    }
}