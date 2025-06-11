package io.github.di

import io.github.ringtonesmartkit.api.RingtoneSmartKit
import io.github.ringtonesmartkit.contract.ActivityTracker
import io.github.ringtonesmartkit.data.applier.RingtoneAssetsApplierImpl
import io.github.ringtonesmartkit.data.applier.RingtoneStorageApplierImpl
import io.github.ringtonesmartkit.data.repository.RingtoneRepositoryImpl
import io.github.ringtonesmartkit.domain.applier.RingtoneAssetsApplier
import io.github.ringtonesmartkit.domain.applier.RingtoneStorageApplier
import io.github.ringtonesmartkit.domain.repository.RingtoneRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


internal val RingtoneModule = module {
    singleOf(::RingtoneRepositoryImpl) { bind<RingtoneRepository>() }
    singleOf(::RingtoneAssetsApplierImpl) { bind<RingtoneAssetsApplier>() }
    singleOf(::RingtoneStorageApplierImpl) { bind<RingtoneStorageApplier>() }
    singleOf(::RingtoneSmartKit)
    single { ActivityTracker }
}