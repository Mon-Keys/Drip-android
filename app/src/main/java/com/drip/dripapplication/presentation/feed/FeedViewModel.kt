package com.drip.dripapplication.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drip.dripapplication.R
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.use_case.GetFeedUseCase
import com.drip.dripapplication.domain.use_case.SetReactionUseCase
import com.drip.dripapplication.domain.model.MatchUserParcelable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val useCaseFeed: GetFeedUseCase,
    private val useCaseReaction: SetReactionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(FeedUIState())
    val uiState: StateFlow<FeedUIState> = _uiState

    private var currentIndex = 0

    private val users = mutableListOf<User>()

    private fun getUsersList(){
        viewModelScope.launch {
            useCaseFeed.execute().collect {
                when (it){
                    is ResultWrapper.Loading -> {
                        Timber.d("Идет загрузка списка пользователей")
                        _uiState.update { currentUiState->
                            currentUiState.copy(isLoading = true, userCard = null, isEndOfList = false, match = null, error = null, isFeedLoadingError = false)
                        }
                    }
                    is ResultWrapper.Error -> {
                        Timber.d("Ошибка при загрузке списка пользователей")
                        if (users.isNotEmpty()) _uiState.update { currentUiState->
                            currentUiState.copy(isLoading = false, error = R.string.error_from_network)
                        }
                        else _uiState.update { currentUiState->
                            currentUiState.copy(isLoading = false, error = R.string.error_from_network,  isFeedLoadingError = true)
                        }
                    }
                    is ResultWrapper.Success -> {
                        Timber.d("Добавляем пользователей в локальный список и получаем текущего")
                        if (it.data == null){
                            _uiState.update { currentUiState ->
                                currentUiState.copy(isEndOfList = true, isLoading = false)
                            }
                        }else{
                            users.clear()
                            users.addAll(it.data)
                            currentIndex = 0
                            getCurrentUser()
                        }
                    }
                }
            }
        }
    }

    fun getCurrentUser(){
        if (users.isEmpty() || (users.isNotEmpty() && currentIndex > users.lastIndex)) {
            getUsersList()
        }else{
            _uiState.update { currentUiState->
                currentUiState.copy(userCard = users[currentIndex], isLoading = false, error = null, match = null)
            }
        }
    }

    fun swipe(userId: Long, reaction:Int){
        viewModelScope.launch {
            useCaseReaction.execute(userId, reaction).collect {
                when(it){
                    is ResultWrapper.Loading ->{
                        Timber.d("Загрузка реакции на свайп")
                        _uiState.update { currentUiState->
                            currentUiState.copy(isLoading = true, userCard = null, isEndOfList = false, match = null, error = null, isFeedLoadingError = false)
                        }
                    }
                    is ResultWrapper.Error -> {
                        Timber.d("Ошибка при получении реакции")
                        _uiState.update { currentUiState->
                            currentUiState.copy(isLoading = false, userCard = users[currentIndex], error = R.string.error_from_network)
                        }
                    }
                    is ResultWrapper.Success -> {
                        if (it.data){
                            Timber.d("Случился match")
                            val user = users[currentIndex]
                            _uiState.update { currentUiState->
                                currentUiState.copy(isLoading = false, match = MatchUserParcelable(user.images.first(), user.name))

                            }
                        }
                        currentIndex += 1
                        getCurrentUser()


                    }
                }
            }

        }

    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
    }
}