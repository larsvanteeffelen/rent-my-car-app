package nl.avans.rentmycar.data.source

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.avans.rentmycar.data.api.CarApi
import nl.avans.rentmycar.data.model.Car

class CarDataSource(
    private val reservationsApi: CarApi,
    private val delayMs: Long = 500
) {
    val fetchCars: Flow<List<Car>> = flow {
        while(true) {
            val reservations = reservationsApi.fetchCars()
            emit(reservations)
            delay(delayMs)
        }
    }
}