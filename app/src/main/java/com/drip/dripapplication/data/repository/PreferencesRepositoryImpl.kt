package com.drip.dripapplication.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.drip.dripapplication.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.Cookie
import timber.log.Timber

class PreferencesRepositoryImpl(private val prefs: SharedPreferences): PreferencesRepository {

    override fun getToken(): String? {
        return prefs.getString(KEY,"")
    }

    override fun saveToken(token: String) {
        prefs.edit().putString(KEY, token).apply()
    }

    override fun deleteAll():Boolean = prefs.edit().clear().commit()



    companion object {
        const val DATASTORE_NAME = "prefData"
        const val KEY = "sessionId"
        private val TOKEN_KEY = stringPreferencesKey("sessionId")
    }

}