package com.drip.dripapplication.presentation.feed

import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.model.MatchUserParcelable

data class FeedUIState(
    val userCard: User? = null,
    val isLoading: Boolean = false,
    val isEndOfList: Boolean = false,
    val isFeedLoadingError: Boolean = false,
    val error: Int? = null,
    val match: MatchUserParcelable? = null
)
