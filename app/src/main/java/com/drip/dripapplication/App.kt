package com.drip.dripapplication

import android.app.Application
import com.drip.dripapplication.data.utils.SharedPrefs
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        SharedPrefs.init(this)

    }
    val appContainer = AppContainer()

}
