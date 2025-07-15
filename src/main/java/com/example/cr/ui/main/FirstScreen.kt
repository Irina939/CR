package com.example.cr.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cr.ui.HeartBackground
import com.example.cr.ui.CRButton

object UiScaffoldDefaults {
    val drawerMenuItems = listOf(
        com.example.cr.DrawerMenuItem("Главная", com.example.cr.Routes.FIRST_SCREEN, com.example.cr.Routes.FIRST_SCREEN, true),
        com.example.cr.DrawerMenuItem("Меню", com.example.cr.Routes.MENU_SCREEN),
        com.example.cr.DrawerMenuItem("Профиль", com.example.cr.Routes.PROFILE_SCREEN),
        com.example.cr.DrawerMenuItem("Корзина", com.example.cr.Routes.CART_SCREEN)
    )
    val logoutMenuItem = com.example.cr.DrawerMenuItem("Выйти из аккаунта", com.example.cr.Routes.WELCOME, "0", true)
    val bottomBarItems = listOf(
        com.example.cr.BottomBarItem({ androidx.compose.material.icons.Icons.Default.Person.let { androidx.compose.material3.Icon(it, contentDescription = "Профиль") } }, "Профиль", com.example.cr.Routes.PROFILE_SCREEN),
        com.example.cr.BottomBarItem({ androidx.compose.material.icons.Icons.Default.Restaurant.let { androidx.compose.material3.Icon(it, contentDescription = "Меню") } }, "Меню", com.example.cr.Routes.MENU_SCREEN),
        com.example.cr.BottomBarItem({ androidx.compose.material.icons.Icons.Default.ShoppingCart.let { androidx.compose.material3.Icon(it, contentDescription = "Корзина") } }, "Корзина", com.example.cr.Routes.CART_SCREEN)
    )
}

@Composable
fun FirstScreenContent(navController: NavController) {
    HeartBackground {
        val context = LocalContext.current
        val db = remember { com.example.cr.data.AppDatabase.getDatabase(context) }
        val userDao = db.userDao()
        var username by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            val sharedPref = context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
            val email = sharedPref.getString("user_email", null)
            if (email != null) {
                val user = userDao.getUserByEmail(email)
                username = user?.name ?: ""
            } else {
                username = ""
            }
        }
        val images = listOf(
            com.example.cr.R.drawable.restoran1,
            com.example.cr.R.drawable.rest5,
            com.example.cr.R.drawable.rest6
        )
        val pagerState = rememberPagerState(pageCount = { images.size })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (username.isNotBlank()) "Привет, $username!" else "Привет!",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .weight(0.2f)
            ) { page ->
                Image(
                    painter = painterResource(id = images[page]),
                    contentDescription = "Фото ресторана",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Row(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            ) {
                repeat(images.size) { index ->
                    val color = if (pagerState.currentPage == index) Color(0xFFE0A8A3) else Color.LightGray
                    Box(
                        Modifier
                            .padding(2.dp)
                            .size(8.dp)
                            .background(color, shape = CircleShape)
                    )
                }
            }
            Text(
                text = "Предлагаем вам погрузиться в мир высоких стандартов обслуживания, изысканной кухни и неповторимой атмосферы, что позволит создать незабываемые впечатления\uD83D\uDE0B",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(18.dp))
            // Горизонтальная панель кнопок
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CRButton(
                    onClick = { navController.navigate(com.example.cr.Routes.FIRST_SCREEN) }
                ) { Text("Главная") }
                CRButton(
                    onClick = { navController.navigate(com.example.cr.Routes.MENU_SCREEN) }
                ) { Text("Меню") }
                CRButton(
                    onClick = { navController.navigate(com.example.cr.Routes.CART_SCREEN) }
                ) { Text("Корзина") }
                CRButton(
                    onClick = { navController.navigate(com.example.cr.Routes.PROFILE_SCREEN) }
                ) { Text("Профиль") }
            }
        }
    }
}