package nl.avans.rentmycar.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun LocationPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    var isPermissionGranted by remember { mutableStateOf(false) }

    // Request location permission launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted
                isPermissionGranted = true
                onPermissionGranted()
            } else {
                // Permission denied
                isPermissionGranted = false
                onPermissionDenied()
            }
        }

    // Use LaunchedEffect to request location permission
    LaunchedEffect(isPermissionGranted) {
        if (!isPermissionGranted) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}
