package nl.avans.rentmycar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.avans.rentmycar.data.repository.CarRepository
import nl.avans.rentmycar.data.repository.UserRepository
import nl.avans.rentmycar.ui.state.CarUIState
import nl.avans.rentmycar.ui.state.UserState

private val _uiState = MutableStateFlow(UserState())

class UserViewModel : ViewModel() {
    val uiState: StateFlow<UserState> = _uiState.asStateFlow()

    init {
        val reservationsRepository = UserRepository()
        viewModelScope.launch {
            reservationsRepository.fetchUser().collect { fetchedUser ->
                _uiState.update { uiState ->
                    uiState.copy(user = fetchedUser)
                }
            }
        }
    }
}