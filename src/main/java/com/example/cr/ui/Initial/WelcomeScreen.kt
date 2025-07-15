package com.example.cr.ui.initial

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cr.ui.HeartBackground
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.cr.ui.CRButton

@Composable
fun WelcomeScreen(navController: NavController) {
    HeartBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Добро пожаловать!")
            Spacer(modifier = Modifier.height(32.dp))
            CRButton(onClick = { navController.navigate("login") }, modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)) {
                Text("Войти")
            }
            Spacer(modifier = Modifier.height(16.dp))
            CRButton(onClick = { navController.navigate("register") }, modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)) {
                Text("Зарегистрироваться")
            }
        }
    }
}