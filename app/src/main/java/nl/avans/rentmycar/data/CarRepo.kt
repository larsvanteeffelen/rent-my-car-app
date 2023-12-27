package nl.avans.rentmycar.data

import nl.avans.rentmycar.model.Car
import nl.avans.rentmycar.network.CarApi
import javax.inject.Inject

class CarRepo @Inject constructor(
    private val carApi: CarApi
) {
    suspend fun getCars(): List<Car> {
        return carApi.getCars()
    }
}