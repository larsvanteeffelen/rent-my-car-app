package nl.avans.rentmycar.data.api

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import nl.avans.rentmycar.client.getBaseURL
import nl.avans.rentmycar.client.getClient
import nl.avans.rentmycar.data.model.Car
import nl.avans.rentmycar.data.model.User

class UserApi {

    private val client = getClient()
    suspend fun fetchUser(): User {

        val response = client.get("http://10.0.2.2:8080/user/1")

        return if (response.status.isSuccess()) {
            Json.decodeFromString<User>(response.bodyAsText())
        } else {
            User(1, "test", "test", "test", "test", "test", 6)
        }
    }
}