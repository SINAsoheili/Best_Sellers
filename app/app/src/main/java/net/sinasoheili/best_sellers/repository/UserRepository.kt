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

            } else {
                emit(DataState.Error(context.getString(R.string.insert_was_not_successful)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun loginUser(phone: String, passwd: String) : Flow<DataState<User>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {

            val userLoginEntity: UserLoginEntity =  webService.loginUser(
                    phone = phone,
                    passwd = passwd
            )

            if (userLoginEntity.loginStatus) { //get user

                try {
                    val user: User? = getUserInfo(userLoginEntity.userId)
                    emit(DataState.Success<User>(user!!))
                    cacheUserId(user.id)
                } catch (e: Exception) {
                    emit(DataState.ConnectionError(e))
                }

            } else {
                emit(DataState.Error(context.getString(R.string.user_was_not_found)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    @Throws(Exception::class) //for connection error
    suspend fun getUserInfo(userId: Int): User? {

        val userInfoEntity : UserInfoEntity = webService.getUserInfo(userId = userId)

        if(userInfoEntity.findStatus) { //user found
            return userMapper.toBase(userInfoEntity.user)
        } else { //user not found with this id
            return null
        }
    }

    private fun cacheUserId(id: Int) {
        CacheToPreference.setWhoLogIn(context , Keys.USER)
        CacheToPreference.setIdPerson(context , id)
    }
}