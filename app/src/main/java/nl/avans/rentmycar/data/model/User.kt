package nl.avans.rentmycar.data.model

import kotlinx.serialization.*

@Serializable
data class User(
    val id: Int? = null,
    var name: String,
    var address: String,
    var zipcode: String,
    var city: String,
    var email: String,
    val drivingScore: Int,
    val authId: String
)