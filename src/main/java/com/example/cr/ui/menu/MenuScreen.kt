package com.example.cr.ui.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cr.MainScaffold
import com.example.cr.ui.HeartBackground
import com.example.cr.ui.main.UiScaffoldDefaults
import com.example.cr.ui.CRButton

@Composable
fun MenuScreenContent(navController: NavController) {
    val viewModel: MenuScreenViewModel = viewModel()
    val categories = viewModel.categories.collectAsState()
    HeartBackground {
        if (categories.value.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val cardTitles = categories.value
                for (row in 0 until 3) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0 until 2) {
                            val index = row * 2 + col
                            if (index < cardTitles.size) {
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 2.dp, vertical = 2.dp)
                                        .width(170.dp)
                                        .height(200.dp)
                                        .clickable {
                                            when (cardTitles[index]) {
                                                "Завтраки" -> navController.navigate("breakfast_screen")
                                                "Салаты" -> navController.navigate("salads_screen")
                                                "Горячее" -> navController.navigate("hotdishes_screen")
                                                "Десерты" -> navController.navigate("desserts_screen")
                                                "Напитки" -> navController.navigate("drinks_screen")
                                                "Закуски" -> navController.navigate("specials_screen")
                                            }
                                        },
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
                                    elevation = CardDefaults.cardElevation(8.dp),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp),
                                    border = BorderStroke(1.dp, Color(0xFFFFFFFF))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        if (cardTitles[index] == "Завтраки") {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Image(
                                                    painter = painterResource(id = com.example.cr.R.drawable.zavtrac),
                                                    contentDescription = "Завтрак",
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxWidth()
                                                        .padding(bottom = 8.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Text(text = cardTitles[index])
                                            }
                                        } else if (cardTitles[index] == "Салаты") {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Image(
                                                    painter = painterResource(id = com.example.cr.R.drawable.salat),
                                                    contentDescription = "Салат",
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxWidth()
                                                        .padding(bottom = 8.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Text(text = cardTitles[index])
                                            }
                                        } else if (cardTitles[index] == "Горячее") {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Image(
                                                    painter = painterResource(id = com.example.cr.R.drawable.gorychee),
                                                    contentDescription = "горячее",
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxWidth()
                                                        .padding(bottom = 8.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Text(text = cardTitles[index])
                                            }
                                        } else if (cardTitles[index] == "Десерты") {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Image(
                                                    painter = painterResource(id = com.example.cr.R.drawable.desert),
                                                    contentDescription = "десерт",
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxWidth()
                                                        .padding(bottom = 8.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Text(text = cardTitles[index])
                                            }
                                        } else if (cardTitles[index] == "Напитки") {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Image(
                                                    painter = painterResource(id = com.example.cr.R.drawable.napitok),
                                                    contentDescription = "напиток",
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxWidth()
                                                        .padding(bottom = 8.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Text(
                                                    text = cardTitles[index],
                                                    color = Color.Black,
                                                    modifier = Modifier.padding(top = 4.dp)
                                                )
                                            }
                                        } else if (cardTitles[index] == "Закуски") {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Image(
                                                    painter = painterResource(id = com.example.cr.R.drawable.zakyski1),
                                                    contentDescription = "закуски",
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxWidth()
                                                        .padding(bottom = 8.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Text(
                                                    text = cardTitles[index],
                                                    color = Color.Black,
                                                    modifier = Modifier.padding(top = 4.dp)
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
    }
}