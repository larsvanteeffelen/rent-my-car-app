package nl.avans.rentmycar.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.avans.rentmycar.ui.viewmodels.CarViewModel

class CarViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarViewModel::class.java)) {
            return CarViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}