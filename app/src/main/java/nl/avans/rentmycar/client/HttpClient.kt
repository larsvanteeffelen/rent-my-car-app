package nl.avans.rentmycar.client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

const val BASE_URL: String = "http://localhost:8080"

fun getClient(): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
}

fun getBaseURL(): String {
    return BASE_URL
}