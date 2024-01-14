import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.avans.rentmycar.components.navigation.AppNavigation
import nl.avans.rentmycar.data.api.UserApi
import nl.avans.rentmycar.data.model.User
import nl.avans.rentmycar.data.repository.UserRepository
import nl.avans.rentmycar.data.source.UserDataSource
import nl.avans.rentmycar.ui.screens.HomeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class OnboardingData(
    var name: String = "",
    var address: String = "",
    var zipcode: String = "",
    var city: String = "",
    var email: String = "",
    var drivingScore: Int = 0,
    var authId: String = ""
)

@Composable
fun OnboardingScreen(authId: String, onComplete: () -> Unit) {
    var currentStep by remember { mutableStateOf(1) }
    val onboardingData = remember { mutableStateOf(OnboardingData()) }

    onboardingData.value.authId = authId
    onboardingData.value.drivingScore = 7

    val userDataSource = remember {
        UserDataSource(UserApi())
    }

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
            when (currentStep) {
                1 -> {
                    Step1(onboardingData) {
                        currentStep = 2
                    }
                }

                2 -> {

                    Step2(onboardingData) {
                        currentStep = 3
                    }
                }

                3 -> {
                    // Use LaunchedEffect to trigger postUserData
                    LaunchedEffect(Unit) {
                        onboardingData.value =
                            onboardingData.value.copy(drivingScore = 7)

                        val onboardingUser = User(
                            name = onboardingData.value.name,
                            address = onboardingData.value.address,
                            zipcode = onboardingData.value.zipcode,
                            city = onboardingData.value.city,
                            email = onboardingData.value.email,
                            drivingScore = onboardingData.value.drivingScore,
                            authId = onboardingData.value.authId
                        )

                        userDataSource.postUser(onboardingUser).collect { result ->
                            if (result != null && result > 0) {
                                onComplete()
                            } else {
                                // Handle the case where the post request failed.
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun Step1(onboardingData: MutableState<OnboardingData>, onNextStep: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Let's get to know you!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "We need some information to get you started.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = onboardingData.value.name,
            onValueChange = { onboardingData.value = onboardingData.value.copy(name = it) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = onboardingData.value.email,
            onValueChange = { onboardingData.value = onboardingData.value.copy(email = it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
//                Verify user data
                if (onboardingData.value.name.isEmpty() || onboardingData.value.email.isEmpty()) {
                    return@Button
                } else {
                    onNextStep()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Continue")
        }
    }
}

@Composable
private fun Step2(onboardingData: MutableState<OnboardingData>, onComplete: () -> Unit) {
    var isLoading by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Nearly there!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "We just need to know your address information.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = onboardingData.value.address,
            onValueChange = { onboardingData.value = onboardingData.value.copy(address = it) },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = onboardingData.value.zipcode,
            onValueChange = { onboardingData.value = onboardingData.value.copy(zipcode = it) },
            label = { Text("Zipcode") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = onboardingData.value.city,
            onValueChange = { onboardingData.value = onboardingData.value.copy(city = it) },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (onboardingData.value.address.isEmpty() || onboardingData.value.zipcode.isEmpty() || onboardingData.value.city.isEmpty()) {
                    return@Button
                } else {
                    onComplete()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading // Disable button while loading
        ) {
            Text("Complete")
        }
    }
}

private suspend fun postUserData(
    onboardingData: OnboardingData,
    userDataSource: UserDataSource,
    onComplete: () -> Unit
) {
    // Use the userDataSource to post user data
    val result = userDataSource.postUser(
        User(
            name = onboardingData.name,
            address = onboardingData.address,
            zipcode = onboardingData.zipcode,
            city = onboardingData.city,
            email = onboardingData.email,
            drivingScore = onboardingData.drivingScore,
            authId = onboardingData.authId
        )
    )
    onComplete()
}


//@Preview
//@Composable
//fun OnboardingScreenPreview() {
//    OnboardingScreen()
//}
