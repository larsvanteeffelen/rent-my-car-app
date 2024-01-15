import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.avans.rentmycar.R
import nl.avans.rentmycar.data.model.Car
import nl.avans.rentmycar.ui.screens.CarAddScreen
import nl.avans.rentmycar.ui.screens.ProfileScreen
import nl.avans.rentmycar.ui.state.UserState
import nl.avans.rentmycar.ui.viewmodels.CarViewModel
import nl.avans.rentmycar.ui.viewmodels.UserViewModel

@Composable
fun SettingsScreen(userViewModel: UserViewModel) {
    var currentPane by remember { mutableStateOf(SettingsPane.Main) }
    val uiState by userViewModel.uiState.collectAsState()


    when (currentPane) {
        SettingsPane.Main -> MainPane(
            onProfileClick = { currentPane = SettingsPane.Profile },
            userViewModel = userViewModel,
            uiState = uiState,
            onAddCarClick = { currentPane = SettingsPane.CarAdd }
        )

        SettingsPane.CarAdd -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                CarAddScreen(userViewModel, uiState.user!!, onLocationDenied = { currentPane = SettingsPane.Main }, onComplete = { currentPane = SettingsPane.Main })
                IconButton(
                    onClick = { currentPane = SettingsPane.Main },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }

        SettingsPane.Profile -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                ProfileScreen(userViewModel)
                IconButton(
                    onClick = { currentPane = SettingsPane.Main },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    }
}

@Composable
private fun MainPane(
    onProfileClick: () -> Unit,
    onAddCarClick: () -> Unit,
    uiState: UserState = UserState(),
    userViewModel: UserViewModel
) {
    var selectedCar by remember { mutableStateOf<Car?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { onProfileClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Account",
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
            )
            Text(
                text = "Account",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                style = MaterialTheme.typography.titleMedium,

                )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Open profile",
                modifier = Modifier
                    .size(32.dp)
            )
        }
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(
                text = "Your Cars",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }
        CarList(
            uiState = uiState,
            userViewModel = userViewModel,
            onCarClick = { selectedCar = it },
            onAddCarClick = { onAddCarClick() }
        )


        if (selectedCar != null) {
            CarEditPane(
                car = selectedCar!!,
                onEditClick = { /* handle editing car details here */ },
                onDeleteClick = { /* handle deleting car here */ }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CarList(
    uiState: UserState,
    userViewModel: UserViewModel,
    onCarClick: (Car) -> Unit,
    onAddCarClick: () -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = uiState.isLoading, onRefresh = {
        userViewModel.refreshUserData(uiState.user!!.authId)
    })

    Column(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxWidth()
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (uiState.userCars.isNullOrEmpty()) {
            Text(
                text = "You don't have any cars yet",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        } else {
            LazyColumn {
                items(uiState.userCars) { car ->
                    CarCard(
                        car = car,
                        onClick = { onCarClick(car) },
                        onDeleteClick = {
                            userViewModel.deleteCar(it)
                            userViewModel.refreshUserData(uiState.user!!.authId)
                        }
                    )
                }
            }
        }

        Button(
            onClick = onAddCarClick,
            modifier = Modifier
                .width(200.dp)
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Car")
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Car"
            )
        }
    }
}

@Composable
private fun CarCard(
    car: Car,
    onClick: () -> Unit,
    onDeleteClick: (car: Car) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = getCarTypePicture(car.type)),
                contentDescription = "Car image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.DirectionsCar,
                contentDescription = "Car"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "${car.make} ${car.model}", style = MaterialTheme.typography.bodyMedium)
        }
        Button(onClick = { onDeleteClick(car) }, modifier = Modifier.padding(8.dp)) {
            Text(text = "Delete")
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Car"
            )
        }



    }

}


@Composable
private fun CarEditPane(
    car: Car,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Implement the car details pane here, similar to the profile pane
    // Include an EditButton and DeleteButton



}

enum class SettingsPane {
    Main,
    Profile,
    CarAdd
}

fun getCarTypePicture(type: String): Int {
    return when (type) {
        "Sedan" -> R.drawable.car_sedan
        "Hatchback" -> R.drawable.car_hatchback
        "SUV" -> R.drawable.car_suv
        "Minivan" -> R.drawable.car_minivan
        "Pickup" -> R.drawable.car_pickup
        "Coupe" -> R.drawable.car_coupe
        "Convertible" -> R.drawable.car_convertible
        else -> R.drawable.car_sedan
    }
}
