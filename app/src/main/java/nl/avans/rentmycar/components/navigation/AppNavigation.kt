package nl.avans.rentmycar.components.navigation

import OnboardingScreen
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nl.avans.rentmycar.data.factory.UserViewModelFactory
import nl.avans.rentmycar.ui.screens.CarScreen
import nl.avans.rentmycar.ui.screens.HomeScreen
import nl.avans.rentmycar.ui.screens.ProfileScreen
import nl.avans.rentmycar.ui.state.UserState
import nl.avans.rentmycar.ui.viewmodels.UserViewModel

@Composable
fun AppNavigation(authId: String) {
    val navController = rememberNavController()
    val currentDestination = navController.currentDestination

    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(authId)
    )

    val uiState by userViewModel.uiState.collectAsState()
    val isLoading = uiState.isLoading
    val user = uiState.user

    LaunchedEffect(user, isLoading) {
        if (!isLoading) {
            if (user == null) {
                navController.navigate("onboarding")
            } else {
                navController.navigate("home")
            }
        }
    }


    Scaffold(bottomBar = {
        NavigationBar {

            navItemList.forEach { navItem ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                    onClick = {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = navItem.label)
                    }
                )
            }
        }
    }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = "home") {
                HomeScreen()
            }
            composable(route = "cars") {
                CarScreen()
            }
            composable(route = "profile") {
                ProfileScreen(userViewModel)
            }
            composable(route = "onboarding") {
                OnboardingScreen(authId) {
                    navController.navigate("home")
                }
            }
        }
    }
}


