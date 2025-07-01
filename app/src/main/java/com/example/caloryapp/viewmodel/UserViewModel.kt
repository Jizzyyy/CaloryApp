package com.example.caloryapp.viewmodel


import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloryapp.model.UserModel
import com.example.caloryapp.repository.UserRepository
import com.example.caloryapp.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    data object Loading : LoginState()
    data class Success(val user: UserModel) : LoginState()
    data class Error(val message: String) : LoginState()
    data object AlreadyLoggedIn : LoginState()
    data object LoggedOut : LoginState()// State baru untuk user yang sudah login
}

class UserViewModel : ViewModel() {
    private val repository = UserRepository()
    private var sessionManager: SessionManager? = null

    private val _registerState = MutableStateFlow<Boolean?>(null)
    val registerState: StateFlow<Boolean?> = _registerState

    // Menyimpan data login state (loading, success, error)
    private val _loginState = mutableStateOf<LoginState>(LoginState.Loading)
    val loginState: State<LoginState> = _loginState

    // Menyimpan data pengguna yang login
    private val _user = mutableStateOf<UserModel?>(null)
    val user: State<UserModel?> = _user

    // Inisialisasi SessionManager
    fun initSessionManager(context: Context) {
        sessionManager = SessionManager(context)
        // Cek apakah user sudah login sebelumnya
        if (sessionManager?.isLoggedIn() == true) {
            _user.value = sessionManager?.getUserData()
            _loginState.value = LoginState.AlreadyLoggedIn
        }
    }

    // Cek apakah user sudah login
    fun checkLoginStatus(): Boolean {
        return sessionManager?.isLoggedIn() ?: false
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            // Fetch user by username from Firestore
            repository.getUserByUsername(username) { fetchedUser ->
                if (fetchedUser != null && fetchedUser.password == password) {
                    _user.value = fetchedUser // Store user data in ViewModel
                    _loginState.value = LoginState.Success(fetchedUser)
                    // Simpan sesi login
                    sessionManager?.saveLoginSession(fetchedUser)
                    Log.d("Login", "Fetched user: $fetchedUser")
                    Log.d("Login", "User fullName: ${fetchedUser.fullName}")
                    Log.d("Login", "User username: ${fetchedUser.username}")
                } else {
                    _loginState.value = LoginState.Error("Invalid username or password")
                }
            }
        }
    }

    // Logout user
    fun logout() {
        try {
            // Gunakan repository untuk logout jika perlu
//            repository.logoutUser()

            // Bersihkan session
            sessionManager?.clearSession()

            // Reset state
            _user.value = null
            _loginState.value = LoginState.LoggedOut

            viewModelScope.launch {
                delay(100) // Delay kecil
                // Baru hapus data user
                _user.value = null
            }


            Log.d("Logout", "User berhasil logout")
        } catch (e: Exception) {
            Log.e("Logout", "Error saat logout: ${e.message}")
            // Tetap set state ke LoggedOut meskipun ada error
            _loginState.value = LoginState.LoggedOut
        }
//        repository.logoutUser()
//        sessionManager?.clearSession()
//        _user.value = null
//        _loginState.value = LoginState.LoggedOut

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
