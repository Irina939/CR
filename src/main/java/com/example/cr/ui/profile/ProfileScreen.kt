package com.example.cr.ui.profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cr.MainScaffold
import com.example.cr.ui.HeartBackground
import com.example.cr.ui.main.UiScaffoldDefaults
import java.io.File
import java.io.FileOutputStream
import com.example.cr.ui.CRButton

fun copyUriToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "profile_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun ProfileScreenContent(navController: NavController) {
    val viewModel: ProfileViewModel = viewModel()
    val username by viewModel.username.collectAsState()
    val profileImageUri by viewModel.profileImageUri.collectAsState()
    val points by viewModel.points.collectAsState()
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userEmail = sharedPref.getString("user_email", null)
    var showSupportDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageConfirmationDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var showPointsInfo by remember { mutableStateOf(false) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                showImageConfirmationDialog = true
            }
        }
    )
    LaunchedEffect(userEmail) {
        viewModel.loadProfile(context)
    }
    HeartBackground {
        if (username == "Загрузка...") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(220.dp)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    val imageModel: Any? = when {
                        selectedImageUri != null -> selectedImageUri
                        !profileImageUri.isNullOrBlank() -> File(profileImageUri!!)
                        else -> "https://www.example.com/placeholder.jpg"
                    }
                    Image(
                        painter = rememberAsyncImagePainter(model = imageModel),
                        contentDescription = "Фото пользователя",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = username.ifBlank { "Имя пользователя" },
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = {
                            newName = username
                            showEditDialog = true
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать имя")
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0A8A3)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Ваши баллы",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            IconButton(
                                onClick = { showPointsInfo = true },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Информация о баллах",
                                    tint = Color.Black
                                )
                            }
                        }
                        Text(
                            text = points.toString(),
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    if (showPointsInfo) {
                        AlertDialog(
                            onDismissRequest = { showPointsInfo = false },
                            title = { Text("Как начисляются баллы?") },
                            text = { Text("Баллы начисляются автоматически: 1 балл = 0,1 от суммы каждого оплаченного заказа.") },
                            confirmButton = {
                                TextButton(onClick = { showPointsInfo = false }) {
                                    Text("Понятно")
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                CRButton(
                    onClick = { showSupportDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(text = "Поддержка")
                }
                CRButton(
                    onClick = { showAboutDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(text = "О сервисе")
                }
                CRButton(
                    onClick = {
                        navController.navigate(com.example.cr.Routes.FIRST_SCREEN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(text = "Назад")
                }
            }
        }
    }

    if (showSupportDialog) {
        AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            title = { Text(text = "Поддержка") },
            text = { Text(text = "Мы здесь, чтобы помочь!\nЗадайте свой вопрос или расскажите о проблеме — мы обязательно найдём решение.\nВаш комфорт — наш приоритет.\n\nСвязаться с нами:\n📩 Напишите сообщение на почту wsrfyh75@gmail.com\n📞 Позвоните по телефону 89024332770\n🌐 Посетите наш сайт\n\nКоманда поддержки всегда рядом, чтобы сделать ваше пользование нашим приложением лучше!") },
            confirmButton = {
                TextButton(
                    onClick = { showSupportDialog = false }
                ) {
                    Text("Закрыть")
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text(text = "О сервисе") },
            text = { Text(text = "Добро пожаловать в мир вкусных впечатлений! Мы создали это приложение, чтобы сделать ваш поход в наш ресторан ещё удобнее и приятнее. Закажите любимые блюда в несколько кликов, узнайте о новинках меню и специальных предложениях — всё это прямо у вас под рукой.\n\nНаши приоритеты — свежие ингредиенты, отличный сервис и уютная атмосфера. Мы стремимся сделать каждое ваше посещение незабываемым!\n\nСпасибо, что выбираете нас — ваш гастрономический путь начинается здесь!") },
            confirmButton = {
                TextButton(
                    onClick = { showAboutDialog = false }
                ) {
                    Text("Закрыть")
                }
            }
        )
    }

    // Диалог для редактирования имени
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Изменить имя") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Новое имя") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateUserName(context, newName)
                    showEditDialog = false
                }) { Text("Сохранить") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Отмена") }
            }
        )
    }

    // Диалог подтверждения картинки
    if (showImageConfirmationDialog && selectedImageUri != null) {
        AlertDialog(
            onDismissRequest = { showImageConfirmationDialog = false; selectedImageUri = null },
            title = { Text(text = "Подтвердите фото") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = rememberAsyncImagePainter(model = selectedImageUri),
                        contentDescription = "Выбранное фото",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val localPath = copyUriToInternalStorage(context, selectedImageUri!!)
                        if (localPath != null) {
                            viewModel.updateProfileImage(context, localPath)
                        }
                        showImageConfirmationDialog = false
                        selectedImageUri = null
                    }
                ) {
                    Text("Загрузить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showImageConfirmationDialog = false; selectedImageUri = null }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
} 