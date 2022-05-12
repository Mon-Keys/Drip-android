package com.drip.dripapplication.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getToken(): Flow<String>

    suspend fun saveToken(token: String)
}