package io.github.ringtonesmartkit.domain.repository

import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget

internal interface RingtoneDataSource {

    fun canHandle(source: RingtoneSource): Boolean
    suspend fun loadRingtone(source: RingtoneSource): RingtoneData
    suspend fun findExistingRingtoneUri(source: RingtoneSource): RingtoneData?
    suspend fun applyRingtones(
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
        source: RingtoneSource,
    )

    suspend fun getSourceType(): RingtoneSource
    suspend fun applyRingtonesToContact(
        source: RingtoneSource,
        target: RingtoneTarget.ContactTarget,
        data: RingtoneData,
    )
}