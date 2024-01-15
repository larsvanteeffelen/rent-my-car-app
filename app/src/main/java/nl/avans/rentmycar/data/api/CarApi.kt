package nl.avans.rentmycar.data.api

import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.util.InternalAPI
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.avans.rentmycar.client.HttpClient
import nl.avans.rentmycar.data.model.Car

class CarApi {

    private val client = HttpClient.client
    suspend fun fetchCars(): List<Car> {

        val response = client.get("http://10.0.2.2:8080/car/all")

        return if (response.status.isSuccess()) {
            try {
                // Check if the response body is not empty
                val responseBody = response.bodyAsText()
                if (responseBody.isNotEmpty() && responseBody != "[]") {
                    Json.decodeFromString<List<Car>>(responseBody)
                } else {
                    // Handle empty response body
                    emptyList()
                }
            } catch (e: SerializationException) {
                // Log the exception
                e.printStackTrace()

                emptyList()
            }
        } else {
            // Log or handle the error based on the response status code
            emptyList()
        }
    }

    suspend fun fetchCarsByOwner(ownerId: Int): List<Car> {
        val response = client.get("http://10.0.2.2:8080/car/all?ownerId=$ownerId")

        return if (response.status.isSuccess()) {
            try {
                // Check if the response body is not empty
                val responseBody = response.bodyAsText()
                if (responseBody.isNotEmpty() && responseBody != "[]") {
                    Json.decodeFromString<List<Car>>(responseBody)
                } else {
                    // Handle empty response body
                    emptyList()
                }
            } catch (e: SerializationException) {
                // Log the exception
                e.printStackTrace()

                emptyList()
            }
        } else {
            // Log or handle the error based on the response status code
            emptyList()
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun createCar(car: Car): Int? {
        try {
            val response = client.request("http://10.0.2.2:8080/car") {
                method = io.ktor.http.HttpMethod.Post
                body = Json.encodeToString(car)
                headers.append("Content-Type", "application/json")
            }

            if (response.status.isSuccess()) {
                val result = response.bodyAsText().toIntOrNull()
                println("Post request successful. Result: $result")
                return result
            } else {
                println("Post request failed. Status: ${response.status}")
                return null
            }
        } catch (e: Exception) {
            println("Post request exception: ${e.message}")
            return null
        }
    }

    suspend fun deleteCar(carId: Int) {
        try {
            val response = client.request("http://10.0.2.2:8080/car/$carId") {
                method = io.ktor.http.HttpMethod.Delete
            }

            if (response.status.isSuccess()) {
                println("Delete request successful.")
            } else {
                println("Delete request failed. Status: ${response.status}")
            }
        } catch (e: Exception) {
            println("Delete request exception: ${e.message}")
        }
    }
}