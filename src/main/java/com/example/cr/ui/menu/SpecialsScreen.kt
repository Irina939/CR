package com.example.cr.ui.menu

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cr.MainScaffold
import com.example.cr.ui.HeartBackground
import com.example.cr.ui.main.UiScaffoldDefaults
import com.example.cr.ui.CRButton

@Composable
fun SpecialsScreenContent(navController: NavController) {
    HeartBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Закуски",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                maxLines = 1,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            val images = listOf(
                com.example.cr.R.drawable.zakyski1,
                com.example.cr.R.drawable.zakyski2,
                com.example.cr.R.drawable.zakyski3,
                com.example.cr.R.drawable.zakyski4
            )
            val customNames = listOf("Закуски 1", "Закуски 2", "Закуски 3", "Закуски 4")
            val prices = listOf("$10", "$8", "$9", "$12")
            for (row in 0 until 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0 until 2) {
                        val index = row * 2 + col
                        if (index < 4) {
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp, vertical = 2.dp)
                                    .width(170.dp)
                                    .height(300.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
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
                                    androidx.compose.foundation.Image(
                                        painter = androidx.compose.ui.res.painterResource(id = images[index]),
                                        contentDescription = customNames[index],
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
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
                                            val item = com.example.cr.ui.CartItem(name = customNames[index], price = prices[index], imageResId = images[index])
                                            com.example.cr.ui.CartManager.addItem(item)
                                        },
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        Text(
                                            "Добавить в корзину",
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
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