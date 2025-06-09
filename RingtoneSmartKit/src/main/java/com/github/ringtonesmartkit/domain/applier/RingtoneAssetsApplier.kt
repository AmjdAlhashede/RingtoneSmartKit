package com.github.ringtonesmartkit.domain.applier

import com.github.ringtonesmartkit.domain.model.RingtoneData
import com.github.ringtonesmartkit.domain.model.RingtoneSource
import com.github.ringtonesmartkit.domain.model.RingtoneTarget

internal interface RingtoneAssetsApplier {
    suspend fun applyAssetsRingtone(
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
        source: RingtoneSource
    )

    suspend fun applyAssetsContact(
        target: RingtoneTarget.ContactTarget,
        ringtone: RingtoneData,
        source: RingtoneSource
    )

}