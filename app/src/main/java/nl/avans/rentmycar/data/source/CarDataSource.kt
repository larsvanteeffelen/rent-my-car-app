package nl.avans.rentmycar.data.source

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.avans.rentmycar.data.api.CarApi
import nl.avans.rentmycar.data.model.Car

class CarDataSource(
    private val carApi: CarApi,
    private val delayMs: Long = 500
) {
    val fetchCars: Flow<List<Car>> = flow {
            val reservations = carApi.fetchCars()
            emit(reservations)
    }

    fun fetchCarsByOwner(ownerId:Int): Flow<List<Car>> = flow {
            val cars = carApi.fetchCarsByOwner(ownerId)
            emit(cars)
    }

    fun createCar(car: Car): Flow<Int> = flow {
        val createdCar = carApi.createCar(car)
        if (createdCar != null) {
            emit(createdCar)
        }
        else{
            emit(0)
        }
    }

    fun deleteCar(carId: Int): Flow<Boolean> = flow {
        carApi.deleteCar(carId)
    }
}