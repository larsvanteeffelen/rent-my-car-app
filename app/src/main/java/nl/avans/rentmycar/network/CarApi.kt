package nl.avans.rentmycar.network

import nl.avans.rentmycar.model.Car
import retrofit2.http.GET

interface CarApi {
    @GET("car/all")
    suspend fun getCars(): List<Car>
}
