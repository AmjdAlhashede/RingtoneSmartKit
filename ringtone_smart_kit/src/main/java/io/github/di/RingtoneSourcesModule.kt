package io.github.di

import dagger.Module
import dagger.Provides
import io.github.ringtonesmartkit.data.source.AssetRingtoneDataSource
import io.github.ringtonesmartkit.data.source.StorageRingtoneDataSource
import io.github.ringtonesmartkit.domain.repository.RingtoneDataSource

@Module
internal object RingtoneSourcesModule {

    @Provides
    fun provideRingtoneDataSources(
        assetDataSource: AssetRingtoneDataSource,
        storageRingtoneDataSource: StorageRingtoneDataSource
    ):List<RingtoneDataSource>{
        return listOf<RingtoneDataSource>(
            assetDataSource,
            storageRingtoneDataSource
        )
    }
}