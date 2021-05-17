package net.sinasoheili.best_sellers.repository

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.User
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.webService.UserMapper
import net.sinasoheili.best_sellers.webService.UserRegisterEntity
import net.sinasoheili.best_sellers.webService.WebService
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

                emit(DataState.Success<User>(userMapper.toBase(userEntity.user)))

            } else {
                emit(DataState.Error(context.getString(R.string.insert_was_not_successful)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }
}