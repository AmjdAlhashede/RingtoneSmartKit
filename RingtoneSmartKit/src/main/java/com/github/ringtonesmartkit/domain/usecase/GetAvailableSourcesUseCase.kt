package com.github.ringtonesmartkit.domain.usecase

import com.github.ringtonesmartkit.domain.model.RingtoneSource
import com.github.ringtonesmartkit.domain.repository.RingtoneRepository

internal class GetAvailableSourcesUseCase(
    private val ringtoneRepository: RingtoneRepository
){
    suspend operator fun invoke(): List<RingtoneSource> {
        return ringtoneRepository.getAvailableSources()
    }
}