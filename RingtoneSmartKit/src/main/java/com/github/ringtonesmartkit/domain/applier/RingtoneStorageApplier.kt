package com.github.ringtonesmartkit.domain.applier

import com.github.ringtonesmartkit.domain.model.RingtoneData
import com.github.ringtonesmartkit.domain.model.RingtoneTarget

internal interface RingtoneStorageApplier {
    suspend fun applyStorageRingtone(target: RingtoneTarget.System, ringtone: RingtoneData)
    suspend fun applyStorageRingtoneContacts(target: RingtoneTarget.ContactTarget, ringtone: RingtoneData)
}