package nl.avans.rentmycar.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
)

val navItemList = listOf<NavItem>(
    NavItem(
        label = "Home",
        icon = Icons.Default.Home,
        route = "home"
    ),
    NavItem(
        label = "Cars",
        icon = Icons.Default.List,
        route = "cars"
    ),
    NavItem(
        label = "Profile",
        icon = Icons.Default.AccountCircle,
        route = "profile"
    )
)