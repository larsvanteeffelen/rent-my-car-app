package nl.avans.rentmycar.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nl.avans.rentmycar.data.factory.UserViewModelFactory
import nl.avans.rentmycar.ui.viewmodels.UserViewModel
import androidx.lifecycle.viewmodel.compose.viewModel



@Composable
fun SettingsScreen(
    userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory()
    )
) {
        val uiState by userViewModel.uiState.collectAsState()
        val user = uiState.user

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if(user == null) {
            Text(text = "No user logged in")
        } else {
//            List of user data
            LazyColumn() {
                item {
                    Text(text = "Name: ${user.name}")
                }
                item {
                    Text(text = "Email: ${user.email}")
                }
                item {
                    Text(text = "Address: ${user.address}")
                }
                item {
                    Text(text = "City: ${user.city}")
                }
                item {
                    Text(text = "Zipcode: ${user.zipcode}")
                }

            }
        }

    }
}