package nl.avans.rentmycar.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.avans.rentmycar.data.repository.CarRepository
import nl.avans.rentmycar.ui.state.CarUIState

private val _uiState = MutableStateFlow(CarUIState())

class CarViewModel : ViewModel() {
    val uiState: StateFlow<CarUIState> = _uiState.asStateFlow()

    init {
        val reservationsRepository = CarRepository()
        viewModelScope.launch {
            reservationsRepository.fetchCars().collect { fetchedCars ->
                _uiState.update { uiState ->
                    uiState.copy(cars = fetchedCars)
                }
            }
        }
    }
}