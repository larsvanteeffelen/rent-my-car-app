package nl.avans.rentmycar.data.source

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nl.avans.rentmycar.data.api.UserApi
import nl.avans.rentmycar.data.model.User

class UserDataSource(
    private val userApi: UserApi,
) {
    fun fetchUser(id: Int): Flow<User?> {
        return flow{
            emit(userApi.fetchUserByID(id))
        }
    }
    fun fetchUserByAuth(authId: String): Flow<User?> {
        return flow {
            emit(userApi.fetchUserByAuthID(authId))
        }
    }

    fun postUser(user: User): Flow<Int?> {
        return flow {
            emit(userApi.postUser(user))
        }
    }
}