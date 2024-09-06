package com.example.coldfiles.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coldfiles.ui.search.SearchScreen
import com.example.coldfiles.ui.settings.SettingsScreen
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
            StorageScreen(
                onSearchClick = { navController.navigate(SearchDestination.route) },
                onSettingsClick = { navController.navigate(SettingsDestination.route) },
            )
        }
        composable(route = SearchDestination.route) {
            SearchScreen()
        }
        composable(route = SettingsDestination.route) {
            SettingsScreen()
        }
    }
}

object StorageDestination : NavigationDestination {
    override val route
        get() = "storage_screen"
    override val title
        get() = "Storage"
}

object SearchDestination : NavigationDestination {
    override val route
        get() = "search_screen"
    override val title
        get() = "Search"
}

object SettingsDestination : NavigationDestination {
    override val route
        get() = "settings_screen"
    override val title
        get() = "Settings"
}