@file:Suppress("NAME_SHADOWING")

package com.example.cr.ui.menu

//noinspection SuspiciousImport
import android.Manifest
import com.example.cr.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import com.example.cr.ui.CartManager
import com.example.cr.ui.HeartBackground
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cr.ui.profile.ProfileViewModel
import com.example.cr.ui.CRButton

@SuppressLint("MissingPermission")
@Composable
fun CartScreenContent(navController: NavController) {
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = viewModel()
    HeartBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(bottom = 100.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Корзина", style = MaterialTheme.typography.titleLarge)
                CartManager.cartItems.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = item.imageResId),
                                    contentDescription = item.name,
                                    modifier = Modifier.size(50.dp)
                                )
                                Column {
                                    Text(text = item.name, style = MaterialTheme.typography.bodyMedium)
                                    Text(text = item.price, style = MaterialTheme.typography.bodySmall)
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Button(
                                        onClick = { CartManager.decreaseItemQuantity(item) },
                                        modifier = Modifier.size(30.dp),
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0A8A3))
                                    ) {
                                        Text("-")
                                    }
                                    Text(
                                        text = item.quantity.value.toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    Button(
                                        onClick = { CartManager.addItem(item) },
                                        modifier = Modifier.size(30.dp),
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0A8A3))
                                    ) {
                                        Text("+")
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = { CartManager.removeItem(item) },
                                    modifier = Modifier.size(40.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0A8A3))
                                ) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Удалить")
                                }
                            }
                        }
                    }
                }
            }

            // Bottom bar for total and order button
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Calculate total price
                val total = remember(CartManager.cartItems.size) {
                    derivedStateOf { 
                        CartManager.cartItems.sumOf { item ->
                            // Assuming price is like "$10" - need to parse the number
                            val priceString = item.price.replace("[^0-9.]".toRegex(), "") // Remove non-numeric chars except dot
                            priceString.toDoubleOrNull()?.let { it * item.quantity.value } ?: 0.0
                        }
                    }
                }

                Text(
                    text = "Всего: $${"%.2f".format(total.value)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                CRButton(
                    onClick = {
                        if (CartManager.cartItems.isNotEmpty()) {
                            val total = CartManager.getTotalPrice()
                            val pointsAdded = (total * 0.1).toInt()
                            profileViewModel.addPoints(context, pointsAdded)
                            showOrderNotification(context, total, pointsAdded)
                            CartManager.clearCart()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Заказать")
                }
            }
        }
    }
}

// Функция для отправки уведомления о заказе
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun showOrderNotification(context: Context, total: Double, pointsAdded: Int) {
    val channelId = "order_channel"
    val channelName = "Order Notifications"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.cr)
        .setContentTitle("Заказ оформлен")
        .setContentText("Сумма заказа: $${"%.2f".format(total)}. Начислено баллов: $pointsAdded. Доставка через 30 минут.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    NotificationManagerCompat.from(context).notify(1, notification)
} 