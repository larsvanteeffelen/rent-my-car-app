package nl.avans.rentmycar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.avans.rentmycar.data.repository.UserRepository
import nl.avans.rentmycar.ui.state.UserState

private val _uiState = MutableStateFlow(UserState(isLoading = true))

class UserViewModel(authId: String) : ViewModel() {
    val uiState: StateFlow<UserState> = _uiState.asStateFlow()

    init {
        loadUserData(authId)
    }

    private fun loadUserData(authId: String) {
        viewModelScope.launch {
            try {
                val userRepository = UserRepository()
                val fetchedUser = userRepository.fetchUserByAuth(authId).firstOrNull()
                _uiState.update { uiState ->
                    uiState.copy(user = fetchedUser, isLoading = false)
                }
            } catch (e: Exception) {
                // Handle exceptions if necessary
                _uiState.update { uiState ->
                    uiState.copy(isLoading = false)
                }
            }
        }
    }
}
