package com.example.cr.ui.initial

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.cr.data.AppDatabase
import com.example.cr.data.User
import com.example.cr.ui.HeartBackground
import kotlinx.coroutines.launch
import androidx.core.content.edit
import com.example.cr.ui.CRButton

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    HeartBackground {
        val context = LocalContext.current
        val db = remember { AppDatabase.getDatabase(context) }
        val userDao = db.userDao()
        val scope = rememberCoroutineScope()
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        // Добавление тестовых пользователей (один раз)
        LaunchedEffect(Unit) {
            val testUsers = listOf(
                User(id = "1", name = "Тестовый 1", email = "test1@example.com", password = "1234"),
                User(id = "2", name = "Тестовый 2", email = "test2@example.com", password = "5678")
            )
            testUsers.forEach { user ->
                if (userDao.getUserByEmail(user.email) == null) {
                    userDao.insert(user)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFFFC8899),
                    unfocusedLabelColor = Color(0xFFFC8899)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFFFC8899),
                    unfocusedLabelColor = Color(0xFFFC8899)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            CRButton(onClick = {
                scope.launch {
                    val user = userDao.getUserByEmail(email)
                    if (user != null && user.password == password) {
                        Toast.makeText(context, "Вход выполнен успешно!", Toast.LENGTH_SHORT).show()
                        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                            sharedPref.edit { putString("user_email", user.email) }
                        }
                        onLoginSuccess()
                    } else {
                        Toast.makeText(context, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
                    }
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Войти")
            }

            Spacer(modifier = Modifier.height(8.dp))

            CRButton(
                onClick = { onNavigateToRegister() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зарегистрироваться")
            }
        }
    }
}