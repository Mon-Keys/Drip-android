package com.drip.dripapplication.data.repository

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.remote.model.UserDto
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.Cridential
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber

class AuthRepositoryImpl(
    private val api: DripApi
) : AuthRepository {
    override fun login(cridential: Cridential): Flow<ResultWrapper<User?>> = flow<ResultWrapper<User?>> {
        emit(ResultWrapper.Loading)
        delay(1000)
        try {
            val response = api.login(cridential.toRepModel())
            Timber.d("$response")
            if (response.isSuccessful && response.body()!=null){
                Timber.d("UserDto=${response.body()}")
                val userDto = response.body()!!.body
                val userDomain = userDto.toDomainModel()
                emit(ResultWrapper.Success(userDomain))
            }else{
                Timber.d("status=${response.isSuccessful}")
                TODO("Берем данные из кэша")
            }

//            emit(ResultWrapper.Success(User(
//                "Алиса",
//                24,
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
//                listOf(
//                    "https://t3.ftcdn.net/jpg/04/64/39/50/240_F_464395072_dodQ5vK3ynIPDbD9nG82clMWJL4zUfMa.jpg",
//                    "https://t4.ftcdn.net/jpg/04/72/09/53/240_F_472095328_nNAedzTl57kxTyzs0wGrkvu3R2MQlvSq.jpg",
//                    "https://t3.ftcdn.net/jpg/04/64/85/42/240_F_464854295_GCNWjI5hPGj8hkWk7Ix8lO0xCSeo2jMc.jpg",
//                    "https://t3.ftcdn.net/jpg/04/64/62/34/240_F_464623498_GteYUp2YtOWCBBoIdUuEBe44Cu4HHZSN.jpg",
//                    "https://t3.ftcdn.net/jpg/04/64/52/06/240_F_464520694_HsFdx2BWUrKRFOdxyM1ATk5wvbqeHttR.jpg",
//                    "https://t4.ftcdn.net/jpg/04/65/40/27/240_F_465402775_ltrRik2AqHgmHz4JgIAHTFFJqWnlRN5F.jpg",
//                    "https://t4.ftcdn.net/jpg/04/69/33/37/240_F_469333729_ohdQnuuAehaGlhWQD1zh4i3MNQb9QMFz.jpg"
//                ),
//                listOf("Учеба","Работа", "Отдых","Друзья","Кино","Путешествия","Книги","Сериалы")
//                )))

        }catch (e: Exception){
            TODO("Берем данные из кэша")
        }
    }
        .flowOn(Dispatchers.IO)
}