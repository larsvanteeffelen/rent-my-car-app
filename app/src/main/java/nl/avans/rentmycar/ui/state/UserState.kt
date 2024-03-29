package nl.avans.rentmycar.ui.state

import nl.avans.rentmycar.data.model.Car
import nl.avans.rentmycar.data.model.User

data class UserState(
   val user: User? = null,
   val isLoading: Boolean = false,
   val userCars: List<Car>? = null
)