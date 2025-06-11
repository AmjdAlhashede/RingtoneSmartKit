package com.studio.ringtonesmartkit.app

import android.app.Application
import io.github.di.initRingtoneSmartKitKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initRingtoneSmartKitKoin()
    }


}