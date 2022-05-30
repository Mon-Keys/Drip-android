package com.drip.dripapplication.data.repository

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.remote.model.UserDto
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import kotlin.random.Random

class UserRepositoryImpl(
    private val api: DripApi
    // private val dao: UserDao
) : UserRepository {
    override fun getUser(): Flow<ResultWrapper<User?>> = flow{
        emit(ResultWrapper.Loading)
        delay(1000)
        try {
            val response = api.getUserInfo()
            Timber.d("$response")
            if (response.isSuccessful && response.body()!=null){
                val status = response.body()!!.status
                val userDto = response.body()!!.body
                val userDomain = userDto?.toDomainModel()

                emit(ResultWrapper.Success(status, userDomain))

                //emit(ResultWrapper.Success(userDomain))

            }else{
                TODO("Берем данные из кэша")
            }

        }catch (e: Exception){
            emit(ResultWrapper.Error(e))
        }
    }
        .flowOn(Dispatchers.IO)


    override fun getFeed(): Flow<ResultWrapper<List<User>?>> = flow{
        emit(ResultWrapper.Loading)
        delay(500)
        try {
            val response = api.getUsers()
            Timber.d("responseBody = ${response.body}")
            if (response.status == 200 && response.body != null){
                val usersDto = response.body.users
                val userDomain = usersDto?.map{
                    it.toDomainModel()
                }
                //emit(ResultWrapper.Success(200,userDomain))

                //Tets
                emit(ResultWrapper.Success(200,testUsers))
            }
        }catch (e: Exception){
            emit(ResultWrapper.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    //TestUsers
    val testUsers = listOf<User>(
        User(Random(1000).nextLong(), "a", 18, "fvfhvevsvreyfvh fhkfr vhgekrbv evyfer", listOf("https://images.pexels.com/photos/12220470/pexels-photo-12220470.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1","https://images.pexels.com/photos/159866/books-book-pages-read-literature-159866.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"), emptyList()),
        User(Random(1000).nextLong(), "b", 19, "fvfhvevsvreyfvh fhkfr vhgekrbv evyfer", listOf("https://images.pexels.com/photos/8147372/pexels-photo-8147372.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1","https://images.pexels.com/photos/4524369/pexels-photo-4524369.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1","https://images.pexels.com/photos/4224258/pexels-photo-4224258.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"), emptyList()),
        User(Random(1000).nextLong(), "c", 20, "fvfhvevsvreyfvh fhkfr vhgekrbv evyfer", listOf("https://images.pexels.com/photos/3330118/pexels-photo-3330118.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"), emptyList()),
        User(Random(1000).nextLong(), "d", 21, "fvfhvevsvreyfvh fhkfr vhgekrbv evyfer", listOf("https://images.pexels.com/photos/4050430/pexels-photo-4050430.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1","https://images.pexels.com/photos/3303491/pexels-photo-3303491.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"), emptyList()),
        User(Random(1000).nextLong(), "e", 22, "fvfhvevsvreyfvh fhkfr vhgekrbv evyfer", listOf("https://images.pexels.com/photos/5997362/pexels-photo-5997362.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1","https://images.pexels.com/photos/11427612/pexels-photo-11427612.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1","https://images.pexels.com/photos/5957128/pexels-photo-5957128.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"), emptyList()),
    )
}
