package com.drip.dripapplication.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.drip.dripapplication.domain.use_case.GetFeedUseCase
import com.drip.dripapplication.domain.use_case.SetReactionUseCase

class ViewModelsFactory(private val useCaseFeed: GetFeedUseCase,
                        private val useCaseReaction: SetReactionUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FeedViewModel(useCaseFeed, useCaseReaction) as T
    }
}