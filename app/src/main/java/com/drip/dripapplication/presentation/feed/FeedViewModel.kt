package com.drip.dripapplication.presentation.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drip.dripapplication.R
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.use_case.GetFeedUseCase
import com.drip.dripapplication.domain.use_case.SetReactionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.random.Random

class FeedViewModel(
    private val useCaseFeed: GetFeedUseCase,
    private val useCaseReaction: SetReactionUseCase
) : ViewModel() {
    private val _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?> = _userInfo

    private val _uiState = MutableStateFlow(FeedUIState())
    val uiState: StateFlow<FeedUIState> = _uiState

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _errorMessage


    private var currentIndex = 0

    private val users = mutableListOf<User>()

    val testUsers = mutableListOf<User>(
        User(Random(1000).nextLong(), "a", 18, "fvfhvevsvreyfvh fhkfr vhgekrbv evyfer", emptyList(), emptyList()),
        User(Random(1000).nextLong(), "b", 19, "fvfhvevsvreyfvh fhkfr vhgekrbv evyfer", emptyList(), emptyList()),
        User(Random(1000).nextLong(), "c", 20, "fvfhvevsvreyfvh fhkfr vhgekrbv evyfer", emptyList(), emptyList()),
        User(Random(1000).nextLong(), "d", 21, "fvfhvevsvreyfvh fhkfr vhgekrbv evyfer", emptyList(), emptyList()),
        User(Random(1000).nextLong(), "e", 22, "fvfhvevsvreyfvh fhkfr vhgekrbv evyfer", emptyList(), emptyList()),
    )

    init {
        getCurrentUser()
    }



    private fun getUsersList(){
        viewModelScope.launch {
            useCaseFeed.execute().collect {
                when (it){
                    is ResultWrapper.Loading -> {
                        Timber.d("Идет загрузка")
                        _uiState.update { currentUiState->
                            currentUiState.copy(isLoading = true, userCard = null, isEndOfList = false)
                        }
                    }
                    is ResultWrapper.Error -> {
                        Timber.d("Ошибка при загрузке данных")
                        _errorMessage.value = R.string.error_from_repository
                    }
                    is ResultWrapper.Success -> {
                        if (it.data == null){
                            _uiState.update { currentUiState ->
                                currentUiState.copy(isEndOfList = true, isLoading = false, userCard = null)
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

    var i = 0;
    private fun getTestUsers(){
        users.clear()
        if (i == 0 || i == 1) {
            users.addAll(testUsers)
            i++
        }else {
            users.addAll(emptyList())
        }
    }


    private fun getCurrentUser(){
        _uiState.update { currentUiState->
            currentUiState.copy(isLoading = true, isEndOfList = false, userCard = null)
        }
        if (users.isEmpty() || (users.isNotEmpty() && currentIndex > users.lastIndex)) {
            getUsersList()
        }else{
            _uiState.update { currentUiState->
                currentUiState.copy(userCard = users[currentIndex], isLoading = false, isEndOfList = false)
            }
        }
    }

    fun swipe(userId: Long, reaction:Int){
        viewModelScope.launch {
            useCaseReaction.execute(userId, reaction)
        }
        currentIndex += 1
        getCurrentUser()

    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
    }
}