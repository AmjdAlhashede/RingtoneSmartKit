package com.github.di

import com.github.ringtonesmartkit.domain.usecase.ApplyContactRingtoneUseCase
import com.github.ringtonesmartkit.domain.usecase.ApplyRingtoneUseCase
import com.github.ringtonesmartkit.domain.usecase.FindExistingRingtoneUriUseCase
import com.github.ringtonesmartkit.domain.usecase.GetAvailableSourcesUseCase
import com.github.ringtonesmartkit.domain.usecase.LoadRingtoneUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val UseCaseModule = module {
    factoryOf(::LoadRingtoneUseCase)
    factoryOf(::ApplyRingtoneUseCase)
    factoryOf(::GetAvailableSourcesUseCase)
    factoryOf(::FindExistingRingtoneUriUseCase)
    factoryOf(::ApplyContactRingtoneUseCase)
}