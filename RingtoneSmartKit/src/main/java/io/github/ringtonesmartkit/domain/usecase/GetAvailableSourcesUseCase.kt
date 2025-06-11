package io.github.ringtonesmartkit.domain.usecase

import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.repository.RingtoneRepository

internal class GetAvailableSourcesUseCase(
    private val ringtoneRepository: RingtoneRepository
){
    suspend operator fun invoke(): List<RingtoneSource> {
        return ringtoneRepository.getAvailableSources()
    }
}