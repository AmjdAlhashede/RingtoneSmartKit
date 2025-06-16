package io.github.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.github.ringtonesmartkit.manager.RingtoneManager
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RingtoneModule::class,
        RingtoneSourcesModule::class,
    ]
)
internal interface RingtoneSmartKitComponent{

    fun provideRingtoneManager(): RingtoneManager

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): RingtoneSmartKitComponent
    }
}