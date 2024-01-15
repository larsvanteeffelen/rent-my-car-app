package nl.avans.rentmycar.ui.screens

import android.telephony.CarrierConfigManager.Gps
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val cars = uiState.cars

    LaunchedEffect(sliderPosition) {
        carViewModel.fetchCarsInRange(sliderPosition.toInt(), 11.11, 11.11)
    }

    Scaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 25,
                    valueRange = 0f..50f
                )
                Text(text = sliderPosition.toInt().toString() + "km")
            }

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
        Icon(Icons.Filled.Info, contentDescription = null, modifier = Modifier.size(64.dp))
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