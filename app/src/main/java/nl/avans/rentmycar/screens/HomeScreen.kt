package nl.avans.rentmycar.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import nl.avans.rentmycar.R
import nl.avans.rentmycar.components.cars.CarCard
import nl.avans.rentmycar.components.cars.CarCardList


@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        CarCardList()
    }
}