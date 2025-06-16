package io.github.ringtonesmartkit.domain.usecase

import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.repository.RingtoneRepository

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
