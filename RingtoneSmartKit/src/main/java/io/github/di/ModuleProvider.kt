package io.github.di

import android.app.Application
import android.content.Context
import io.github.ringtonesmartkit.contract.ActivityTracker
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

internal val ringtoneSmartKitModule = module {
    includes(RingtoneModule, UseCaseModule, RingtoneSourcesModule, ViewModelModules)
}


fun Context.initRingtoneSmartKitKoin() {
    (this as? Application)?.registerActivityLifecycleCallbacks(ActivityTracker)

    startKoin {
        androidContext(this@initRingtoneSmartKitKoin)
        modules(
            ringtoneSmartKitModule
        )
    }
}

