package com.drip.dripapplication.di

import com.drip.dripapplication.domain.repository.*
import com.drip.dripapplication.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetFeedUseCase(repository: FeedRepository): GetFeedUseCase = GetFeedUseCase(repository)

    @Provides
    fun provideGetUserInfoUseCase(repository: UserRepository): GetUserInfoUseCase = GetUserInfoUseCase(repository)

    @Provides
    fun provideSetReactionUseCase(repository: LikeRepository): SetReactionUseCase = SetReactionUseCase(repository)

    @Provides
    fun provideEditProfileUseCase(repository: ProfileRepository): EditProfileUseCase = EditProfileUseCase(repository)

    @Provides
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase = LoginUseCase(repository)

    @Provides
    fun provideSignUpUseCase(repository: AuthRepository): SignupUseCase = SignupUseCase(repository)
}