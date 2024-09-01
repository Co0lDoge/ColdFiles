package com.example.coldfiles.ui.navigation

/** Interface that describes navigation destination for app's screens**/
interface NavigationDestination {
    val route: String // Value, used for navigation
    val title: String // Title for display
}