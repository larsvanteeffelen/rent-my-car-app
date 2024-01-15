package nl.avans.rentmycar.ui.screens

import android.location.Location
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
import androidx.compose.runtime.MutableState
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
fun CarAddScreen(userViewModel: UserViewModel, owner: User, onLocationDenied: () -> Unit, onComplete: () -> Unit) {
    val carData = remember { mutableStateOf(Car(null, owner.id!!, 0.0, 0.0, "", "", 0.0, "")) }

    var location by remember { mutableStateOf<Location?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    LocationPermissionHandler(
        onPermissionGranted = {
            coroutineScope.launch {
                location = LocationUtils.getCurrentLocation(context).firstOrNull()
                carData.value = carData.value.copy(latitude = location?.latitude ?: 0.0, longitude = location?.longitude ?: 0.0)
            }
        },
        onPermissionDenied = {
            onLocationDenied()
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CarInfoSection(carData)

            Spacer(modifier = Modifier.height(16.dp))

            AddButton(
                text = stringResource(id = R.string.add_car),
                onClick = {
                    userViewModel.addCar(carData.value)
                    onComplete()
                }
            )
        }
    }
}

@Composable
private fun CarInfoSection(carData: MutableState<Car>) {
    // Convert Car to MutableState<Car>
    LazyColumn {
        item {
            MakeDropdown(
                make = carData.value.make,
                onMakeChange = { carData.value = carData.value.copy(make = it) }
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_model),
                value = carData.value.model,
                onValueChange = { carData.value = carData.value.copy(model = it) }
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_rental_price),
                value = carData.value.rentalPrice.toString(),
                onValueChange = {
                    val price = it.toDoubleOrNull() ?: 0.0
                    carData.value = carData.value.copy(rentalPrice = price)
                }
            )
        }
        item {
            TypeDropdown(
                type = carData.value.type,
                onTypeChange = { carData.value = carData.value.copy(type = it) }
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
