package nl.avans.rentmycar.models.response

import kotlinx.serialization.Serializable

@Serializable
data class CarResponse(
    val id: Int? = null,
    val make: String,
    val model: String,
    val type: String,
    val rentalPrice: Double,
    val latitude: Double,
    val longitude: Double
)
