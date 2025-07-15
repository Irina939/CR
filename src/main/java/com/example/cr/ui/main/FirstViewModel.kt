package com.example.cr.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FirstViewModel : ViewModel() {
    private val _username = MutableStateFlow("Загрузка...")
    val username: StateFlow<String> = _username

    init {
        loadUsername()
    }

    fun loadUsername() {
        viewModelScope.launch {
            delay(1000)
            _username.value = "Имя пользователя"
        }
    }
} 