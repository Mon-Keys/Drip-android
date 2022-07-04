package com.drip.dripapplication.domain.repository

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import okhttp3.Cookie

interface PreferencesRepository {
    fun getToken(): String?

    fun saveToken(token: String)

    fun deleteAll(): Boolean

}