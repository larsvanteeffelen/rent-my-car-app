package nl.avans.rentmycar.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class CarRequest(
    val make: String,
    val model: String,
    val type: String,
    val rentalPrice: Double,
    val latitude: Double,
    val longitude: Double
)
