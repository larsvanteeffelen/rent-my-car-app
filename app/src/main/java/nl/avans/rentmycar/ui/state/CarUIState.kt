package nl.avans.rentmycar.ui.state

import nl.avans.rentmycar.data.model.Car

data class CarUIState(
    val cars: List<Car> = emptyList()
)