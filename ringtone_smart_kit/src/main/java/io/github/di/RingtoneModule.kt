package io.github.di

import dagger.Binds
import dagger.Module
import io.github.ringtonesmartkit.data.applier.RingtoneAssetsApplierImpl
import io.github.ringtonesmartkit.data.applier.RingtoneStorageApplierImpl
import io.github.ringtonesmartkit.data.repository.RingtoneRepositoryImpl
import io.github.ringtonesmartkit.domain.applier.RingtoneAssetsApplier
import io.github.ringtonesmartkit.domain.applier.RingtoneStorageApplier
import io.github.ringtonesmartkit.domain.repository.RingtoneRepository

@Module
internal abstract class RingtoneModule {

    @Binds
    abstract fun provideRingtoneRepository(
        ringtoneRepositoryImpl: RingtoneRepositoryImpl
    ): RingtoneRepository

    @Binds
    abstract fun provideRingtoneAssetsApplier(
        ringtoneAssetsApplierImpl: RingtoneAssetsApplierImpl
    ) : RingtoneAssetsApplier

    @Binds
    abstract fun provideRingtoneStorageApplier(
        ringtoneStorageApplierImpl : RingtoneStorageApplierImpl
    ) : RingtoneStorageApplier


}