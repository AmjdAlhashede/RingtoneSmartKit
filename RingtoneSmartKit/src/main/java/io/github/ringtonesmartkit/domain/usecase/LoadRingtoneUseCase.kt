package io.github.ringtonesmartkit.domain.usecase

import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.repository.RingtoneRepository


internal class LoadRingtoneUseCase(private val repository: RingtoneRepository) {
    suspend operator fun invoke(source: RingtoneSource): RingtoneData? {
        return repository.getRingtone(source)
    }
}