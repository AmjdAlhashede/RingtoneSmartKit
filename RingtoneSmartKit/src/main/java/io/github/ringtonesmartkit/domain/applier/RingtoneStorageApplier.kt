package io.github.ringtonesmartkit.domain.applier

import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneTarget

internal interface RingtoneStorageApplier {
    suspend fun applyStorageRingtone(target: RingtoneTarget.System, ringtone: RingtoneData)
    suspend fun applyStorageRingtoneContacts(target: RingtoneTarget.ContactTarget, ringtone: RingtoneData)
}