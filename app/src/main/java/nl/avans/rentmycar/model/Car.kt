package nl.avans.rentmycar.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Car(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val make: String,
    val model: String,
    val rentalPrice: Double,
    val type: String
)