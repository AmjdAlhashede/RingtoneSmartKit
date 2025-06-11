package io.github.di

import io.github.ringtonesmartkit.domain.usecase.ApplyContactRingtoneUseCase
import io.github.ringtonesmartkit.domain.usecase.ApplyRingtoneUseCase
import io.github.ringtonesmartkit.domain.usecase.FindExistingRingtoneUriUseCase
import io.github.ringtonesmartkit.domain.usecase.GetAvailableSourcesUseCase
import io.github.ringtonesmartkit.domain.usecase.LoadRingtoneUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val UseCaseModule = module {
    factoryOf(::LoadRingtoneUseCase)
    factoryOf(::ApplyRingtoneUseCase)
    factoryOf(::GetAvailableSourcesUseCase)
    factoryOf(::FindExistingRingtoneUriUseCase)
    factoryOf(::ApplyContactRingtoneUseCase)
}