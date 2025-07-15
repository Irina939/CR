package com.example.cr.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.cr.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel() {
    private val _username = MutableStateFlow("Загрузка...")
    val username: StateFlow<String> = _username
    private val _profileImageUri = MutableStateFlow<String?>(null)
    val profileImageUri: StateFlow<String?> = _profileImageUri
    private val _points = MutableStateFlow(0)
    val points: StateFlow<Int> = _points

    fun loadProfile(context: Context) {
        viewModelScope.launch {
            _username.value = "Загрузка..."
            val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val email = sharedPref.getString("user_email", null)
            if (email != null) {
                val db = AppDatabase.getDatabase(context)
                val user = withContext(Dispatchers.IO) { db.userDao().getUserByEmail(email) }
                _username.value = user?.name ?: "Имя пользователя"
                _profileImageUri.value = user?.profileImageUri
                _points.value = user?.points ?: 0
            } else {
                _username.value = "Имя пользователя"
                _profileImageUri.value = null
                _points.value = 0
            }
        }
    }

    fun updateUserName(context: Context, newName: String) {
        viewModelScope.launch {
            val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val email = sharedPref.getString("user_email", null)
            if (email != null) {
                val db = AppDatabase.getDatabase(context)
                val user = withContext(Dispatchers.IO) { db.userDao().getUserByEmail(email) }
                if (user != null) {
                    val updatedUser = user.copy(name = newName)
                    withContext(Dispatchers.IO) { db.userDao().insert(updatedUser) }
                    _username.value = newName
                    _profileImageUri.value = updatedUser.profileImageUri
                }
            }
        }
    }

    fun updateProfileImage(context: Context, imageUri: String) {
        viewModelScope.launch {
            val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val email = sharedPref.getString("user_email", null)
            if (email != null) {
                val db = AppDatabase.getDatabase(context)
                val user = withContext(Dispatchers.IO) { db.userDao().getUserByEmail(email) }
                if (user != null) {
                    withContext(Dispatchers.IO) { db.userDao().updateProfileImage(user.id, imageUri) }
                    _profileImageUri.value = imageUri
                }
            }
        }
    }

    fun addPoints(context: Context, pointsToAdd: Int) {
        viewModelScope.launch {
            val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val email = sharedPref.getString("user_email", null)
            if (email != null) {
                val db = AppDatabase.getDatabase(context)
                val user = withContext(Dispatchers.IO) { db.userDao().getUserByEmail(email) }
                if (user != null) {
                    val newPoints = user.points + pointsToAdd
                    withContext(Dispatchers.IO) { db.userDao().updatePoints(user.id, newPoints) }
                    _points.value = newPoints
                }
            }
        }
    }
} 