package nl.avans.rentmycar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.avans.rentmycar.data.model.Car
import nl.avans.rentmycar.data.model.User
import nl.avans.rentmycar.data.repository.CarRepository
import nl.avans.rentmycar.data.repository.UserRepository
import nl.avans.rentmycar.ui.state.UserState

private val _uiState = MutableStateFlow(UserState(isLoading = true))

class UserViewModel(authId: String) : ViewModel() {
    val uiState: StateFlow<UserState> = _uiState.asStateFlow()
    val isRefreshing: Boolean
        get() = uiState.value.isLoading
    init {
        loadUserData(authId)
    }

    private fun loadUserData(authId: String) {
        viewModelScope.launch {
            try {
                val userRepository = UserRepository()
                val carRepository = CarRepository()
                val fetchedUser = userRepository.fetchUserByAuth(authId).firstOrNull()
                val fetchedCars = fetchedUser!!.id?.let { carRepository.fetchCarsByOwner(it).firstOrNull() }
                _uiState.update { uiState ->
                    uiState.copy(user = fetchedUser, isLoading = false, userCars = fetchedCars)
                }
            } catch (e: Exception) {
                // Handle exceptions if necessary
                _uiState.update { uiState ->
                    uiState.copy(isLoading = false)
                }
            }
        }
    }

    fun refreshUserData(authId: String) {
        _uiState.update { uiState ->
            uiState.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val userRepository = UserRepository()
                val carRepository = CarRepository()
                val fetchedUser = userRepository.fetchUserByAuth(authId).firstOrNull()
                val fetchedCars = fetchedUser!!.id?.let { carRepository.fetchCarsByOwner(it).firstOrNull() }
                _uiState.update { uiState ->
                    uiState.copy(user = fetchedUser, isLoading = false, userCars = fetchedCars)
                }
            } catch (e: Exception) {
                // Handle exceptions if necessary
                _uiState.update { uiState ->
                    uiState.copy(isLoading = false)
                }
            }
        }
    }

    // Add a function to update the user if needed
    fun updateUser(user: User) {
        viewModelScope.launch {
            val userRepository = UserRepository()
            userRepository.updateUser(user).collect{ result ->
                if (result) {
                    val fetchedUser = userRepository.fetchUserByAuth(user.authId).firstOrNull()
                    _uiState.update { uiState ->
                        uiState.copy(user = fetchedUser, isLoading = false)
                    }
                }
            }
        }
    }

    fun addCar(car: Car) {
        viewModelScope.launch {
            val carRepository = CarRepository()
            carRepository.createCar(car).collectIndexed { index, result ->
                if (result != null) {
                    val fetchedCars = carRepository.fetchCarsByOwner(car.ownerId).firstOrNull()
                    _uiState.update { uiState ->
                        uiState.copy(userCars = fetchedCars, isLoading = false)
                    }
                }
            }
        }
    }
}
