package nl.avans.rentmycar.network

import nl.avans.rentmycar.model.Car
import retrofit2.http.GET

interface ApiService {
    @GET("cars")
    suspend fun getCars(): List<Car>
}
