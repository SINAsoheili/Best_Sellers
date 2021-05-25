package net.sinasoheili.best_sellers.repository

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.User
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.util.Keys
import net.sinasoheili.best_sellers.util.CacheToPreference
import net.sinasoheili.best_sellers.webService.*
import java.lang.Exception

class UserRepository constructor(
    private val context: Context,
    private val webService: WebService,
    private val userMapper: UserMapper
    )
{
    suspend fun registerUser(user: User, passwd: String) : Flow<DataState<User>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {

            val userEntity: UserRegisterEntity =  webService.registerUser(
                name = user.name,
                lastName = user.lastName,
                phone = user.phone,
                passwd = passwd
            )

            if (userEntity.statusRegister) {

                val user: User = userMapper.toBase(userEntity.user)
                emit(DataState.Success<User>(user))
                cacheUserId(user.id)
                setUserToCache(user)

            } else {
                emit(DataState.Error(context.getString(R.string.insert_was_not_successful)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun loginUser(phone: String, passwd: String) : Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {

            val userLoginEntity: UserLoginEntity =  webService.loginUser(
                    phone = phone,
                    passwd = passwd
            )

            if (userLoginEntity.loginStatus) { // user found of server

                emit(DataState.Success<Boolean>(true))
                cacheUserId(userLoginEntity.userId)

            } else {
                emit(DataState.Error(context.getString(R.string.user_not_found)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun getUserInfo(): Flow<DataState<User>> = flow {

        val user: User? = getUserFromCache()
        if (user != null) {
            emit(DataState.Success<User>(user))
        } else {
            emit(DataState.Loading())
            delay(1000)

            try {
                val userId: Int = getUserIdFromCache()
                val userInfoEntity: UserInfoEntity = webService.getUserInfo(userId)
                if (userInfoEntity.findStatus) { //user found
                    val userFetched: User = UserMapper().toBase(userInfoEntity.user)
                    cacheUserId(userFetched.id)
                    setUserToCache(userFetched)
                    emit(DataState.Success(userFetched))
                } else { // user not found
                    emit(DataState.Error(context.getString(R.string.user_not_found)))
                }
            } catch (e: Exception) {
                emit(DataState.ConnectionError(e))
            }
        }
    }

    private fun cacheUserId(id: Int) {
        CacheToPreference.setWhoLogIn(context , Keys.USER)
        CacheToPreference.setIdPerson(context , id)
    }

    private fun getUserIdFromCache(): Int {
        val who: String? = CacheToPreference.getWhoLogIn(context)
        if (who == null) {
             return -1
        } else {
            if (who.equals(Keys.USER)){
                return CacheToPreference.getPersonId(context)
            } else {
                return -1
            }
        }
    }

    private fun setUserToCache(user: User) {
        CacheToPreference.storeUser(context, user)
    }

    private fun getUserFromCache(): User? {
        return CacheToPreference.fetchUser(context)
    }
}