package nl.avans.rentmycar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.avans.rentmycar.R
import nl.avans.rentmycar.data.api.UserApi
import nl.avans.rentmycar.data.model.User
import nl.avans.rentmycar.data.source.UserDataSource
import nl.avans.rentmycar.ui.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel
) {
    val uiState by userViewModel.uiState.collectAsState()
    var name by remember { mutableStateOf(uiState.user?.name ?: "") }
    var email by remember { mutableStateOf(uiState.user?.email ?: "") }
    var address by remember { mutableStateOf(uiState.user?.address ?: "") }
    var city by remember { mutableStateOf(uiState.user?.city ?: "") }
    var zipcode by remember { mutableStateOf(uiState.user?.zipcode ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.user == null) {
            Text(text = stringResource(id = R.string.no_user_logged_in))
        } else {
            Text(text = stringResource(id = R.string.your_info), style = MaterialTheme.typography.titleMedium)
            UserInfoSection(
                name = name,
                email = email,
                address = address,
                city = city,
                zipcode = zipcode,
                onNameChange = { name = it },
                onEmailChange = { email = it },
                onAddressChange = { address = it },
                onCityChange = { city = it },
                onZipcodeChange = { zipcode = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            EditButton(
                text = stringResource(id = R.string.update_user_info),
                icon = Icons.Default.Edit,
                onClick = {
                    if (name.isEmpty() || email.isEmpty() || address.isEmpty() || city.isEmpty() || zipcode.isEmpty()) {
                        return@EditButton
                    }
                    userViewModel.updateUser(User(
                        authId = uiState.user!!.authId,
                        name = name,
                        email = email,
                        address = address,
                        city = city,
                        zipcode = zipcode,
                        drivingScore = uiState.user!!.drivingScore,
                        id = uiState.user!!.id
                    ))
                }
            )
            Spacer(modifier = Modifier.height(30.dp))
            LogoutButton(onLogoutClick = { /* TODO: Handle logout */ })
        }
    }
}

@Composable
private fun UserInfoSection(
    name: String,
    email: String,
    address: String,
    city: String,
    zipcode: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onZipcodeChange: (String) -> Unit
) {
    LazyColumn {
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_name),
                value = name,
                onValueChange = { onNameChange(it) }
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_email),
                value = email,
                onValueChange = { onEmailChange(it) }
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_address),
                value = address,
                onValueChange = { onAddressChange(it) }
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_city),
                value = city,
                onValueChange = { onCityChange(it) }
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_zipcode),
                value = zipcode,
                onValueChange = { onZipcodeChange(it) }
            )
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
private fun EditButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    val buttonColor = Color(android.graphics.Color.parseColor("#82DD55")) // Replace with your desired hex color code
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(buttonColor, CircleShape)
            .clickable { onClick() }
            .padding(bottom = 8.dp, top = 8.dp, start = 16.dp, end = 16.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, color = Color.White, modifier = Modifier.weight(1f))
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
    }
}

@Composable
private fun LogoutButton(onLogoutClick: () -> Unit) {
    Button(
        onClick = { onLogoutClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = stringResource(id = R.string.logout), color = Color.Red)
        }
    }
}
