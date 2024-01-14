package nl.avans.rentmycar.data.api

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.isSuccess
import io.ktor.util.InternalAPI
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.avans.rentmycar.client.getBaseURL
import nl.avans.rentmycar.client.getClient
import nl.avans.rentmycar.data.model.Car
import nl.avans.rentmycar.data.model.User

class UserApi {

    private val client = getClient()
    suspend fun fetchUserByID(id:Int): User? {

        val response = client.get("http://10.0.2.2:8080/user/${id}")

        return if (response.status.isSuccess()) {
            Json.decodeFromString<User>(response.bodyAsText())
        } else {
            null
        }
    }
    suspend fun fetchUserByAuthID(authId: String): User? {

        val response = client.get("http://10.0.2.2:8080/user?authId=${authId}")

        return if (response.status.isSuccess()) {
            Json.decodeFromString<User>(response.bodyAsText())
        } else {
            null
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun postUser(user: User): Int? {
        try {
            val response = client.request("http://10.0.2.2:8080/user") {
                method = io.ktor.http.HttpMethod.Post
                body = Json.encodeToString(user)
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
}