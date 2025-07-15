package com.example.cr

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.CRTheme
import com.example.cr.ui.initial.LoginScreen
import com.example.cr.ui.initial.RegisterScreen
import com.example.cr.ui.initial.WelcomeScreen
import com.example.cr.ui.main.FirstScreenContent
import com.example.cr.ui.menu.BreakfastScreenContent
import com.example.cr.ui.menu.CartScreenContent
import com.example.cr.ui.menu.DessertsScreenContent
import com.example.cr.ui.menu.DrinksScreenContent
import com.example.cr.ui.menu.HotDishesScreenContent
import com.example.cr.ui.menu.MenuScreenContent
import com.example.cr.ui.menu.SaladsScreenContent
import com.example.cr.ui.menu.SpecialsScreenContent
import com.example.cr.ui.profile.ProfileScreenContent
import kotlinx.coroutines.launch

// --- Data classes for menu items ---
data class DrawerMenuItem(
    val label: String,
    val route: String,
    val popUpTo: String? = null,
    val inclusive: Boolean = false
)

data class BottomBarItem(
    val icon: @Composable () -> Unit,
    val description: String,
    val route: String
)

// --- Drawer Content ---
@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    scope: kotlinx.coroutines.CoroutineScope,
    items: List<DrawerMenuItem>,
    logoutItem: DrawerMenuItem
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label, color = Color.Black) },
                selected = false,
                onClick = {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(item.route) {
                            item.popUpTo?.let { popUpTo(it) { inclusive = item.inclusive } }
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        NavigationDrawerItem(
            label = { Text(logoutItem.label, color = Color.Black) },
            selected = false,
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate(logoutItem.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        )
    }
}

// --- Bottom Bar ---
@Composable
fun MainBottomBar(navController: NavController, items: List<BottomBarItem>) {
    NavigationBar(containerColor = Color(0xFFE0A8A3)) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { item.icon.invoke() },
                selected = false,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}

// --- Main Scaffold ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    title: String,
    drawerMenuItems: List<DrawerMenuItem>,
    logoutMenuItem: DrawerMenuItem,
    bottomBarItems: List<BottomBarItem>,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.background(Color(0xFFE0A8A3))
            ) {
                DrawerContent(navController, drawerState, scope, drawerMenuItems, logoutMenuItem)
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = { Text(title, color = Color.Black) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE0A8A3))
                )
                Box(modifier = Modifier.weight(1f)) { content() }
                MainBottomBar(navController, bottomBarItems)
            }
        }
    }
}

// --- MainActivity ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CRTheme {
                val notificationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        // Можно обработать результат, если нужно
                    }
                )
                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
                val navController = rememberNavController()

                // Drawer and BottomBar items
                val drawerMenuItems = listOf(
                    DrawerMenuItem("Главная", Routes.FIRST_SCREEN, Routes.FIRST_SCREEN, true),
                    DrawerMenuItem("Меню", Routes.MENU_SCREEN),
                    DrawerMenuItem("Профиль", Routes.PROFILE_SCREEN),
                    DrawerMenuItem("Корзина", Routes.CART_SCREEN)
                )
                val logoutMenuItem = DrawerMenuItem("Выйти из аккаунта", Routes.WELCOME, "0", true)
                val bottomBarItems = listOf(
                    BottomBarItem({ Icon(Icons.Default.Person, contentDescription = "Профиль", tint = Color.Black) }, "Профиль", Routes.PROFILE_SCREEN),
                    BottomBarItem({ Icon(Icons.Default.Restaurant, contentDescription = "Меню", tint = Color.Black) }, "Меню", Routes.MENU_SCREEN),
                    BottomBarItem({ Icon(Icons.Default.ShoppingCart, contentDescription = "Корзина", tint = Color.Black) }, "Корзина", Routes.CART_SCREEN)
                )

                NavHost(navController = navController, startDestination = Routes.WELCOME) {
                    composable(Routes.WELCOME) { WelcomeScreen(navController) }
                    composable(Routes.LOGIN) {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate(Routes.FIRST_SCREEN) {
                                    popUpTo(Routes.WELCOME) { inclusive = true }
                                }
                            },
                            onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
                        )
                    }
                    composable(Routes.REGISTER) {
                        RegisterScreen(navController = navController) {
                            navController.navigate(Routes.FIRST_SCREEN) {
                                popUpTo(Routes.WELCOME) { inclusive = true }
                            }
                        }
                    }
                    composable(Routes.FIRST_SCREEN) {
                        MainScaffold(
                            navController,
                            "Главная",
                            drawerMenuItems,
                            logoutMenuItem,
                            bottomBarItems
                        ) { FirstScreenContent(navController) }
                    }
                    composable(Routes.MENU_SCREEN) {
                        MainScaffold(
                            navController,
                            "Меню",
                            drawerMenuItems,
                            logoutMenuItem,
                            bottomBarItems
                        ) { MenuScreenContent(navController) }
                    }
                    composable(Routes.PROFILE_SCREEN) {
                        MainScaffold(
                            navController,
                            "Профиль",
                            drawerMenuItems,
                            logoutMenuItem,
                            bottomBarItems
                        ) { ProfileScreenContent(navController) }
                    }
                    composable(Routes.BREAKFAST_SCREEN) {
                        MainScaffold(
                            navController,
                            "Завтраки",
                            drawerMenuItems,
                            logoutMenuItem,
                            bottomBarItems
                        ) { BreakfastScreenContent(navController) }
                    }
                    composable(Routes.SALADS_SCREEN) {
                        MainScaffold(
                            navController,
                            "Салаты",
                            drawerMenuItems,
                            logoutMenuItem,
                            bottomBarItems
                        ) { SaladsScreenContent(navController) }
                    }
                    composable(Routes.HOTDISHES_SCREEN) {
                        MainScaffold(
                            navController,
                            "Горячее",
                            drawerMenuItems,
                            logoutMenuItem,
                            bottomBarItems
                        ) { HotDishesScreenContent(navController) }
                    }
                    composable(Routes.DESSERTS_SCREEN) {
                        MainScaffold(
                            navController,
                            "Десерты",
                            drawerMenuItems,
                            logoutMenuItem,
                            bottomBarItems
                        ) { DessertsScreenContent(navController) }
                    }
                    composable(Routes.DRINKS_SCREEN) {
                        MainScaffold(
                            navController,
                            "Напитки",
                            drawerMenuItems,
                            logoutMenuItem,
                            bottomBarItems
                        ) { DrinksScreenContent(navController) }
                    }
                    composable(Routes.SPECIALS_SCREEN) {
                        MainScaffold(
                            navController,
                            "Закуски",
                            drawerMenuItems,
                            logoutMenuItem,
                            bottomBarItems
                        ) { SpecialsScreenContent(navController) }
                    }
                    composable(Routes.CART_SCREEN) {
                        MainScaffold(
                            navController,
                            "Корзина",
                            drawerMenuItems,
                            logoutMenuItem,
                            bottomBarItems
                        ) { CartScreenContent(navController) }
                    }
                }
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CRTheme {
        FirstScreenContent(rememberNavController())
    }
}

// --- Routes ---
object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FIRST_SCREEN = "first_screen"
    const val MENU_SCREEN = "menu_screen"
    const val PROFILE_SCREEN = "profile_screen"
    const val BREAKFAST_SCREEN = "breakfast_screen"
    const val SALADS_SCREEN = "salads_screen"
    const val HOTDISHES_SCREEN = "hotdishes_screen"
    const val DESSERTS_SCREEN = "desserts_screen"
    const val DRINKS_SCREEN = "drinks_screen"
    const val SPECIALS_SCREEN = "specials_screen"
    const val CART_SCREEN = "cart_screen"
}
