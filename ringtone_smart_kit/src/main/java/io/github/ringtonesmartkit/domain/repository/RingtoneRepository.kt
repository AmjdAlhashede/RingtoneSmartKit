package io.github.ringtonesmartkit.domain.repository

import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget

internal interface RingtoneRepository {
    suspend fun getAvailableSources(): List<RingtoneSource>
    suspend fun getRingtone(source: RingtoneSource): RingtoneData?
    suspend fun applyRingtones(
        source: RingtoneSource,
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
    )

    suspend fun applyContactRingtone(
        source: RingtoneSource,
        target: RingtoneTarget.ContactTarget,
        data: RingtoneData,
    )
}
