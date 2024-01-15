package nl.avans.rentmycar.data.repository

import kotlinx.coroutines.flow.Flow
import nl.avans.rentmycar.data.api.CarApi
import nl.avans.rentmycar.data.model.Car
import nl.avans.rentmycar.data.source.CarDataSource

class CarRepository {
    private val carApi = CarApi()
    private val carDataSource: CarDataSource = CarDataSource(carApi)
    fun fetchCars(): Flow<List<Car>> {
        return carDataSource.fetchCars
    }
    fun fetchCarsByOwner(ownerId:Int): Flow<List<Car>> {
        return carDataSource.fetchCarsByOwner(ownerId)
    }

    fun createCar(car: Car): Flow<Int> {
        return carDataSource.createCar(car)
    }
}