package nl.avans.rentmycar.client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

object HttpClient {
    const val BASE_URL: String = "http://localhost:8080"

    val client: HttpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }
}