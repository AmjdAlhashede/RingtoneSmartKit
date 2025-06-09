package com.github.di

import com.github.ringtonesmartkit.viewmodules.ContactPickerViewModel
import com.github.ringtonesmartkit.data.source.AssetRingtoneDataSource
import com.github.ringtonesmartkit.data.source.StorageRingtoneDataSource
import com.github.ringtonesmartkit.domain.repository.RingtoneDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val RingtoneSourcesModule = module {
    single<List<RingtoneDataSource>> {
        listOf(
            get<AssetRingtoneDataSource>(),
            get<StorageRingtoneDataSource>()
        )
    }


    singleOf(::AssetRingtoneDataSource) { bind<RingtoneDataSource>() }
    singleOf(::StorageRingtoneDataSource) { bind<RingtoneDataSource>() }

}