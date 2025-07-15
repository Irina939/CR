package com.example.cr.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cr.MainScaffold
import com.example.cr.R
import com.example.cr.ui.CartItem
import com.example.cr.ui.CartManager
import com.example.cr.ui.HeartBackground
import com.example.cr.ui.main.UiScaffoldDefaults
import com.example.cr.ui.CRButton

@Composable
fun HotDishesScreenContent(navController: NavController) {
    HeartBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Горячее",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                maxLines = 1,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            val items = List(10) { i -> "Горячее ${i + 1}" }
            val images = listOf(
                R.drawable.gorychee,
                R.drawable.gorychee1,
                R.drawable.gorychee2,
                R.drawable.gorychee3,
                R.drawable.gorychee4,
                R.drawable.gorychee5
            )
            val customNames = listOf("Медвежий пир", "Классика вкуса", "Летний бриз", "Вихрь", "Вечерняя звезда", "Вулкан")
            val prices = listOf("$23", "$18", "$20", "$22", "$18", "$25")
            for (row in 0 until 5) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0 until 2) {
                        val index = row * 2 + col
                        if (index < items.size) {
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp, vertical = 2.dp)
                                    .width(170.dp)
                                    .height(300.dp),
                                colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFFFFFFFF)),
                                elevation = CardDefaults.cardElevation(8.dp),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (index < 6) {
                                        Image(
                                            painter = painterResource(id = images[index]),
                                            contentDescription = items[index],
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth(),
                                            contentScale = ContentScale.Crop
                                        )
                                        Text(
                                            text = customNames[index],
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                        Text(
                                            text = prices[index],
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                        CRButton(
                                            onClick = { 
                                                val item = CartItem(name = customNames[index], price = prices[index], imageResId = images[index])
                                                CartManager.addItem(item)
                                            },
                                            modifier = Modifier.padding(top = 8.dp)
                                        ) {
                                            Text(
                                                "Добавить в корзину",
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    } else {
                                        Text(text = items[index])
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 