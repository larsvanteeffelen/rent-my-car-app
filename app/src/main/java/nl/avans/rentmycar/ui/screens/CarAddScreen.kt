package nl.avans.rentmycar.ui.screens

import LocationScreen
import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.firstOrNull
import nl.avans.rentmycar.R
import nl.avans.rentmycar.data.model.Car
import nl.avans.rentmycar.data.model.User
import nl.avans.rentmycar.ui.viewmodels.UserViewModel
import nl.avans.rentmycar.utils.LocationUtils
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import nl.avans.rentmycar.utils.LocationPermissionHandler

@Composable
fun CarAddScreen(userViewModel: UserViewModel, owner: User, onLocationDenied: () -> Unit ) {
    var car by remember { mutableStateOf(Car(null, owner.id!!, 0.0, 0.0, "", "", 0.0, "")) }

    var location by remember { mutableStateOf<Location?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    LocationPermissionHandler(
        onPermissionGranted = {
            // Permission granted, fetch the location
            coroutineScope.launch {
                location = LocationUtils.getCurrentLocation(context).firstOrNull()
                car = car.copy(latitude = location?.latitude ?: 0.0, longitude = location?.longitude ?: 0.0)
                Log.d("LocationPermission", "Location: $location")
            }
        },
        onPermissionDenied = {
            Log.d("LocationPermission", "Permission denied")
//            Go back to settings screen

        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.add_car),
            style = MaterialTheme.typography.titleMedium
        )
        CarInfoSection(
            car = car,
            onMakeChange = { car = car.copy(make = it) },
            onModelChange = { car = car.copy(model = it) },
            onRentalPriceChange = { car = car.copy(rentalPrice = it) }
        ) { car = car.copy(type = it) }

        Spacer(modifier = Modifier.height(16.dp))
        AddButton(
            text = stringResource(id = R.string.add_car),
            onClick = {
                if (car.make == "" || car.model == "" || car.type == "" || car.rentalPrice == 0.0) {
                    return@AddButton
                } else {
                    userViewModel.addCar(car)
                }
            }
        )
    }
}

@Composable
private fun CarInfoSection(
    car: Car, // Change the parameter type to Car
    onMakeChange: (String) -> Unit,
    onModelChange: (String) -> Unit,
    onRentalPriceChange: (Double) -> Unit,
    onTypeChange: (String) -> Unit
) {
    // Convert Car to MutableState<Car>
    val carState = remember { mutableStateOf(car) }

    LazyColumn {
        item {
            MakeDropdown(
                make = carState.value.make,
                onMakeChange = onMakeChange
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_model),
                value = carState.value.model,
                onValueChange = onModelChange
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_rental_price),
                value = carState.value.rentalPrice.toString(),
                onValueChange = {
                    val price = it.toDoubleOrNull() ?: 0.0
                    onRentalPriceChange(price)
                }
            )
        }
        item {
            TypeDropdown(
                type = carState.value.type,
                onTypeChange = onTypeChange
            )
        }
    }
}


@Composable
fun MakeDropdown(
    make: String,
    onMakeChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // List of common car makes
    val carMakes =
        listOf("Toyota", "Honda", "Ford", "Chevrolet", "Volkswagen", "BMW", "Mercedes-Benz")

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp)
                .clip(MaterialTheme.shapes.small)
                .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
        ) {
            Text(
                text = make.ifEmpty { "Select Make" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Icon(
                imageVector = if (expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
            )
        }

        if (expanded) {
            CarMakeList(
                carMakes = carMakes,
                onMakeSelected = {
                    onMakeChange(it)
                    expanded = false
                }
            )
        }
    }
}

@Composable
private fun CarMakeList(
    carMakes: List<String>,
    onMakeSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
            .clip(MaterialTheme.shapes.small)
    ) {
        carMakes.forEach { make ->
            Text(
                text = make,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMakeSelected(make) }
                    .padding(16.dp)
            )
            Divider(color = Color.Gray)
        }
    }
}

@Composable
fun TypeDropdown(
    type: String,
    onTypeChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // List of common car types
    val carTypes = listOf("Sedan", "Hatchback", "SUV", "Minivan", "Pickup", "Coupe", "Convertible")

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp)
                .clip(MaterialTheme.shapes.small)
                .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
        ) {
            Text(
                text = type.ifEmpty { "Select Type" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Icon(
                imageVector = if (expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
            )
        }

        if (expanded) {
            CarTypeList(
                carTypes = carTypes,
                onTypeSelected = {
                    onTypeChange(it)
                    expanded = false
                }
            )
        }
    }
}

@Composable
private fun CarTypeList(
    carTypes: List<String>,
    onTypeSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
            .clip(MaterialTheme.shapes.small)
    ) {
        carTypes.forEach { type ->
            Text(
                text = type,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTypeSelected(type) }
                    .padding(16.dp)
            )
            Divider(color = Color.Gray)
        }
    }
}

@Composable
private fun EditableTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
private fun AddButton(text: String, onClick: () -> Unit) {
    val buttonColor = Color(android.graphics.Color.parseColor("#82DD55"))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(buttonColor, CircleShape)
            .clickable { onClick() }
            .padding(bottom = 8.dp, top = 8.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, color = Color.White, modifier = Modifier.weight(1f))
    }
}
