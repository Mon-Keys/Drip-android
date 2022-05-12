package com.drip.dripapplication.data.repository

import android.content.Context
import androidx.datastore.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.drip.dripapplication.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>): PreferencesRepository {

    companion object {
        const val DATASTORE_NAME = "prefData"
        private val TOKEN_KEY = stringPreferencesKey("sessionId")
    }

    override fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN_KEY].orEmpty()
        }
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

}