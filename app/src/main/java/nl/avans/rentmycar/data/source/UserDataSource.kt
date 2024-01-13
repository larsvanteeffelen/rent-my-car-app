package nl.avans.rentmycar.data.source

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.avans.rentmycar.data.api.UserApi
import nl.avans.rentmycar.data.model.User

class UserDataSource(
    private val userApi: UserApi,
    private val delayMs: Long = 500
) {
    val fetchUser: Flow<User?> = flow {
        val reservations = userApi.fetchUser()
        emit(reservations)
        delay(delayMs)
    }


}