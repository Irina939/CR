package com.example.cr.ui.initial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel : ViewModel() {
    private val _welcomeText = MutableStateFlow("Загрузка...")
    val welcomeText: StateFlow<String> = _welcomeText

    init {
        loadWelcome()
    }

    fun loadWelcome() {
        viewModelScope.launch {
            delay(1000)
            _welcomeText.value = "Добро пожаловать!"
        }
    }
} 