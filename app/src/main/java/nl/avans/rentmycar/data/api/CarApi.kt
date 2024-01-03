package nl.avans.rentmycar.data.api

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import nl.avans.rentmycar.client.getBaseURL
import nl.avans.rentmycar.client.getClient
import nl.avans.rentmycar.data.model.Car

class CarApi {

    private val client = getClient()
    suspend fun fetchCars(): List<Car> {

        val response = client.get("${getBaseURL()}/car/all")

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
                // Log the exception or handle it appropriately
                emptyList()
            }
        } else {
            // Log or handle the error based on the response status code
            emptyList()
        }
    }
}