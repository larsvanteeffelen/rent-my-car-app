package nl.avans.rentmycar.ui.car

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.avans.rentmycar.data.CarRepo
import nl.avans.rentmycar.model.Car
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val carRepo: CarRepo
): ViewModel(){
    private val _state = MutableStateFlow(emptyList<Car>())
    val state: StateFlow<List<Car>>
        get() = _state

    init {
        viewModelScope.launch {
            val cars = carRepo.getCars()
            _state.value
        }
    }
}