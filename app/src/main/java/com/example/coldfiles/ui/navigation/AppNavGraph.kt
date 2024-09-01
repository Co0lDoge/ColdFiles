package com.example.coldfiles.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coldfiles.ui.storage.StorageScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    /** Composable that manages screen navigation **/
    NavHost(
        navController = navController,
        startDestination = StorageDestination.route,
        modifier = modifier
    ) {
        composable(route = StorageDestination.route) {
            StorageScreen()
        }
    }
}

object StorageDestination : NavigationDestination {
    override val route
        get() = "storage_screen"
    override val title
        get() = "home"
}