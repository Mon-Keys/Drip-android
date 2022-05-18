package com.drip.dripapplication.presentation.feed

import com.drip.dripapplication.domain.model.User

data class FeedUIState(
    val userCard: User? = null,
    val isLoading: Boolean = false,
    val isEndOfList: Boolean = false
)