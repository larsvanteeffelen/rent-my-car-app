package nl.avans.rentmycar.data.repository

import kotlinx.coroutines.flow.Flow
import nl.avans.rentmycar.data.api.UserApi
import nl.avans.rentmycar.data.model.User
import nl.avans.rentmycar.data.source.UserDataSource

class UserRepository {
    private val userApi = UserApi()
    private val userDataSource: UserDataSource = UserDataSource(userApi)
    fun fetchUser(): Flow<User?> {
        return userDataSource.fetchUser
    }
}