/*
 * Copyright 2025 Amjd Alhashede
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.github.ringtonesmartkit.api

import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.usecase.ApplyContactRingtoneUseCase
import io.github.ringtonesmartkit.domain.usecase.ApplyRingtoneUseCase
import io.github.ringtonesmartkit.domain.usecase.LoadRingtoneUseCase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class RingtoneSmartKit @Inject constructor(
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