package nl.avans.rentmycar.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nl.avans.rentmycar.components.cars.CarCardList

// viewModel: MainViewModel = viewModel()
@Composable
fun CarsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CarCardList()
        Text(text = "Cars screen")
    }
}