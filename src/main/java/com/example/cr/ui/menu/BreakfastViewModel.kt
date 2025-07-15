package com.example.cr.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BreakfastViewModel : ViewModel() {
    private val _breakfasts = MutableStateFlow<List<String>>(emptyList())
    val breakfasts: StateFlow<List<String>> = _breakfasts

    init {
        loadBreakfasts()
    }

    fun loadBreakfasts() {
        viewModelScope.launch {
            delay(1000) // имитация долгой загрузки
            _breakfasts.value = List(10) { i -> "Завтрак ${i + 1}" }
        }
    }
} 