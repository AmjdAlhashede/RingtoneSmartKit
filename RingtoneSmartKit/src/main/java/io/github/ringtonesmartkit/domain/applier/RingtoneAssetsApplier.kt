package io.github.ringtonesmartkit.domain.applier

import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget

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