package com.github.ringtonesmartkit.domain.usecase

import com.github.ringtonesmartkit.domain.model.RingtoneData
import com.github.ringtonesmartkit.domain.model.RingtoneSource
import com.github.ringtonesmartkit.domain.repository.RingtoneRepository


internal class LoadRingtoneUseCase(private val repository: RingtoneRepository) {
    suspend operator fun invoke(source: RingtoneSource): RingtoneData? {
        return repository.getRingtone(source)
    }
}