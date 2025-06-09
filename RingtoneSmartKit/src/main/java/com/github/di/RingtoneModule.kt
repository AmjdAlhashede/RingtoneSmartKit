package com.github.di

import com.github.ringtonesmartkit.api.RingtoneSmartKit
import com.github.ringtonesmartkit.contract.ActivityTracker
import com.github.ringtonesmartkit.data.applier.RingtoneAssetsApplierImpl
import com.github.ringtonesmartkit.data.applier.RingtoneStorageApplierImpl
import com.github.ringtonesmartkit.data.repository.RingtoneRepositoryImpl
import com.github.ringtonesmartkit.domain.applier.RingtoneAssetsApplier
import com.github.ringtonesmartkit.domain.applier.RingtoneStorageApplier
import com.github.ringtonesmartkit.domain.repository.RingtoneRepository
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