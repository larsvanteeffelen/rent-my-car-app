package nl.avans.rentmycar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.avans.rentmycar.R
import nl.avans.rentmycar.data.factory.UserViewModelFactory
import nl.avans.rentmycar.data.model.User
import nl.avans.rentmycar.ui.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory()
    )
) {
    val uiState by userViewModel.uiState.collectAsState()
    val user = uiState.user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user == null) {
            Text(text = stringResource(id = R.string.no_user_logged_in))
        } else {
            Text(text = stringResource(id = R.string.your_info), style = MaterialTheme.typography.titleMedium)
            UserInfoSection(
                user = user,
                onNameChange = { /* Handle name change */ },
                onEmailChange = { /* Handle email change */ },
                onAddressChange = { /* Handle address change */ },
                onCityChange = { /* Handle city change */ },
                onZipcodeChange = { /* Handle zipcode change */ }
            )
            Spacer(modifier = Modifier.height(16.dp))
            EditButton(
                text = stringResource(id = R.string.update_user_info),
                icon = Icons.Default.ExitToApp,
                onClick = { /* Handle update user info click */ }
            )
            Spacer(modifier = Modifier.height(30.dp))
            LogoutButton(onLogoutClick = { /* Handle logout */ })
        }
    }
}

@Composable
private fun UserInfoSection(
    user: User,
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
                value = user.name,
                onValueChange = onNameChange
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_email),
                value = user.email,
                onValueChange = onEmailChange
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_address),
                value = user.address,
                onValueChange = onAddressChange
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_city),
                value = user.city,
                onValueChange = onCityChange
            )
        }
        item {
            EditableTextField(
                label = stringResource(id = R.string.edit_zipcode),
                value = user.zipcode,
                onValueChange = onZipcodeChange
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

