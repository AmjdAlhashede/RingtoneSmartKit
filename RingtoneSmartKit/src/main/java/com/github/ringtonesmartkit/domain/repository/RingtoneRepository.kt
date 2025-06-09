package com.github.ringtonesmartkit.domain.repository

import com.github.ringtonesmartkit.domain.model.RingtoneData
import com.github.ringtonesmartkit.domain.model.RingtoneSource
import com.github.ringtonesmartkit.domain.model.RingtoneTarget

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
