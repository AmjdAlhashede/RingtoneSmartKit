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

import io.github.ringtonesmartkit.domain.model.ContactInfo
import io.github.ringtonesmartkit.domain.model.ContactTarget
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.SystemTarget
import io.github.ringtonesmartkit.domain.repository.RingtoneResultHandler

import io.github.ringtonesmartkit.provider.RingtoneSmartKitInitProvider


object RingtoneHelper {

    private val ringtoneManager = RingtoneSmartKitInitProvider.component.provideRingtoneManager()

    /**
     * Sets the system ringtone (calls, notifications, alarms).
     *
     * @param source Ringtone source (asset, storage, etc.).
     * @param target SystemTarget : CALL, NOTIFICATION, ALARM.
     * @return SystemRingtoneResultHandler with async callbacks.
     */
    @JvmStatic
    fun setSystemRingtone(
        source: RingtoneSource,
        target: SystemTarget,
    ): RingtoneResultHandler<Unit> {
        return ringtoneManager.setSystemRingtone(source, target)
    }

    /**
     * Sets a ringtone for a specific contact.
     *
     * @param source Ringtone source.
     * @param target ContactTarget identifier (ById, ByPhone, Interactive, etc.).
     * @return ContactRingtoneResultHandler with async callbacks.
     */
    @JvmStatic
    fun setContactRingtone(
        source: RingtoneSource,
        target: ContactTarget,
    ): RingtoneResultHandler<ContactInfo> {
        return ringtoneManager.setContactRingtone(source, target)
    }
}