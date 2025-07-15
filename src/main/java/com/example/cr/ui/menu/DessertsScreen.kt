package com.example.cr.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cr.MainScaffold
import com.example.cr.ui.CartItem
import com.example.cr.ui.CartManager
import com.example.cr.ui.HeartBackground
import com.example.cr.ui.main.UiScaffoldDefaults
import com.example.cr.ui.CRButton

@Composable
fun DessertsScreenContent(navController: NavController) {
    HeartBackground {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Десерты",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                maxLines = 1,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            val images = listOf(
                com.example.cr.R.drawable.desert1,
                com.example.cr.R.drawable.desert2,
                com.example.cr.R.drawable.desert3,
                com.example.cr.R.drawable.desert,
                com.example.cr.R.drawable.desert4,
                com.example.cr.R.drawable.desert5,
                com.example.cr.R.drawable.desert6,
                com.example.cr.R.drawable.desert7
            )
            val customNames = listOf("Лето", "Гора фантазий", "Тайна тропиков", "Гармония", "Десерт 5", "Десерт 6", "Десерт 7", "Десерт 8")
            val prices = listOf("$22", "$15", "$18", "$20", "$15", "$16", "$18", "$25")
            for (row in 0 until 4) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0 until 2) {
                        val index = row * 2 + col
                        if (index < 8) {
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp, vertical = 2.dp)
                                    .width(170.dp)
                                    .height(300.dp)
                                    .clickable {
                                        // обработчик нажатия, если потребуется
                                    },
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
                                    Image(
                                        painter = painterResource(id = images[index]),
                                        contentDescription = customNames[index],
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
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 