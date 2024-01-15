package nl.avans.rentmycar.data.repository

import kotlinx.coroutines.flow.Flow
import nl.avans.rentmycar.data.api.UserApi
import nl.avans.rentmycar.data.model.User
import nl.avans.rentmycar.data.source.UserDataSource

class UserRepository {
    private val userApi = UserApi()
    private val userDataSource: UserDataSource = UserDataSource(userApi)
    fun fetchUserById(id: Int): Flow<User?> {
        return userDataSource.fetchUser(id)
    }
    fun fetchUserByAuth(authId: String): Flow<User?> {
        return userDataSource.fetchUserByAuth(authId)
    }

    fun postUser(user: User): Flow<Int?> {
        return userDataSource.postUser(user)
    }

    fun updateUser(user: User): Flow<Boolean> {
        return userDataSource.updateUser(user)
    }

}