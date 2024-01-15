import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import nl.avans.rentmycar.R
import nl.avans.rentmycar.utils.LocationUtils

@Composable
fun LocationScreen() {
    var location by remember { mutableStateOf<Location?>(null) }
    var isPermissionGranted by remember { mutableStateOf(false) }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                isPermissionGranted = true
            } else {
                isPermissionGranted = false
            }
        }

    val coroutineScope = rememberCoroutineScope()

    // Capture LocalContext within a Composable function
    val context = LocalContext.current

    // Use LaunchedEffect to request location permission and fetch location
    LaunchedEffect(isPermissionGranted) {
        if (!isPermissionGranted) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            coroutineScope.launch {
                location = LocationUtils.getCurrentLocation(context).firstOrNull()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (location == null) {
            Button(
                onClick = {
                    // Request location permission
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.get_location))
            }
        } else {
            Text(
                text = "Latitude: ${location?.latitude}\nLongitude: ${location?.longitude}",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            IconButton(
                onClick = {
//                    // Refresh location
//                    location = LocationUtils.getCurrentLocation(LocalContext.current)
//                        .firstOrNull()
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}
