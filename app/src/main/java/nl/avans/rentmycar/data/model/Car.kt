package nl.avans.rentmycar.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Car(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val make: String,
    val model: String,
    val rentalPrice: Double,
    val type: String
)