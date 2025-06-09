package com.github.ringtonesmartkit.api

import com.github.ringtonesmartkit.domain.model.RingtoneData
import com.github.ringtonesmartkit.domain.model.RingtoneSource
import com.github.ringtonesmartkit.domain.model.RingtoneTarget
import com.github.ringtonesmartkit.domain.usecase.ApplyContactRingtoneUseCase
import com.github.ringtonesmartkit.domain.usecase.ApplyRingtoneUseCase
import com.github.ringtonesmartkit.domain.usecase.LoadRingtoneUseCase


internal class RingtoneSmartKit(
    private val loadRingtone: LoadRingtoneUseCase,
    private val applyRingtone: ApplyRingtoneUseCase,
    private val applyContactRingtoneUseCase: ApplyContactRingtoneUseCase,
) {

    /**
     * يقوم بتحميل النغمة من المصدر المحدد ثم يطبقها على الهدف.
     */
    suspend fun loadAndApply(
        source: RingtoneSource,
        target: RingtoneTarget,
    ) {

        when (target) {
            is RingtoneTarget.System -> {
                val ringtone = load(source)
                ringtone?.let { applyToRingtone(source = source, target = target, ringtone = it) }
            }

            is RingtoneTarget.ContactTarget -> {
                val ringtone = load(source)
                ringtone?.let { applyToContacts(source = source, target = target, ringtone = it) }
            }
        }
    }

    /**
     * تحميل النغمة فقط بدون تطبيقها.
     */
    suspend fun load(source: RingtoneSource) = loadRingtone(source)

    /**
     * تطبيق نغمة معينة (قمت بتحميلها مسبقًا).
     */
    suspend fun applyToRingtone(
        source: RingtoneSource,
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
    ) {
        applyRingtone(source = source, target = target, ringtone = ringtone)
    }

    suspend fun applyToContacts(
       source: RingtoneSource,
       target: RingtoneTarget.ContactTarget,
       ringtone: RingtoneData
    ){
        applyContactRingtoneUseCase(
            source = source,
            target = target,
            ringtone = ringtone
        )
    }
}