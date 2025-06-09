package com.github.ringtonesmartkit.domain.repository

import com.github.ringtonesmartkit.domain.model.RingtoneData
import com.github.ringtonesmartkit.domain.model.RingtoneSource
import com.github.ringtonesmartkit.domain.model.RingtoneTarget

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