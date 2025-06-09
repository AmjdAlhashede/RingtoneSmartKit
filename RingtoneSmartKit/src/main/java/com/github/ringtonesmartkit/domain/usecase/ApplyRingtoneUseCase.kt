package com.github.ringtonesmartkit.domain.usecase

import com.github.ringtonesmartkit.domain.model.RingtoneData
import com.github.ringtonesmartkit.domain.model.RingtoneSource
import com.github.ringtonesmartkit.domain.model.RingtoneTarget
import com.github.ringtonesmartkit.domain.repository.RingtoneRepository

internal class ApplyRingtoneUseCase(
    private val ringtoneRepository: RingtoneRepository,
) {
    suspend operator fun invoke(
        source: RingtoneSource,
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
    ) {
        ringtoneRepository.applyRingtones(source = source, target = target, ringtone = ringtone)
    }
}
