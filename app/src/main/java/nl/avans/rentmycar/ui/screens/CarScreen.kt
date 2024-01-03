package nl.avans.rentmycar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nl.avans.rentmycar.data.factory.CarViewModelFactory
import nl.avans.rentmycar.data.model.Car
import nl.avans.rentmycar.ui.viewmodels.CarViewModel


//object ItemEntryDestination : NavigationDestination {
//    override val route = "reservations"
//    override val titleRes = R.string.reservations
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarScreen(
    carViewModel: CarViewModel = viewModel(
        factory = CarViewModelFactory()
    )
) {
    val uiState by carViewModel.uiState.collectAsState()
    val cars = uiState.cars

    Scaffold(

    ) {
        LazyColumn(contentPadding = it) {
            items(cars) { car ->
                CarItem(
                    car = car,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun CarItem(
    car: Car,
    modifier: Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Icon(Icons.Filled.ThumbUp, contentDescription = null, modifier = Modifier.size(64.dp))
        Column {
            Row(modifier = modifier) {
                Text(
                    text = "${car.make} - ${car.rentalPrice}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(modifier = modifier) {
                Text(
                    text = "${car.type} ${car.longitude} - ${car.latitude}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}