package com.studio.ringtonesmartkit.app

import android.app.Application
import com.github.di.initRingtoneSmartKitKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initRingtoneSmartKitKoin()
    }


}