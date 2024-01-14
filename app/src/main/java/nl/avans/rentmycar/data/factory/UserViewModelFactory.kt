package nl.avans.rentmycar.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nl.avans.rentmycar.ui.viewmodels.CarViewModel
import nl.avans.rentmycar.ui.viewmodels.UserViewModel

class UserViewModelFactory(authId:String) : ViewModelProvider.Factory {
    private val authId = authId
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(authId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}