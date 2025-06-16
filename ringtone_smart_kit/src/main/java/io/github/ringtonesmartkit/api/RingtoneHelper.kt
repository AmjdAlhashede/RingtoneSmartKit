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

import io.github.provider.RingtoneSmartKitInitProvider
import io.github.ringtonesmartkit.domain.model.ContactIdentifier
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.model.RingtoneType

/**
 * Helper object that provides a simplified API for setting system and contact ringtones.
 *
 * Uses the underlying RingtoneManager implementation from the DI provider.
 */
object RingtoneHelper {

    private val ringtoneManager = RingtoneSmartKitInitProvider.component.provideRingtoneManager()

    /**
     * Sets the device's system ringtone from a given source.
     *
     * @param source The source of the ringtone (e.g., local file, URL, or resource).
     * @param type The type of system ringtone to set (CALL, NOTIFICATION, or ALARM). Default is [RingtoneType.CALL].
     * @param onSuccess Callback invoked if the ringtone is set successfully.
     * @param onError Callback invoked with a [Throwable] if an error occurs during the operation.
     */
    fun setSystemRingtone(
        source: RingtoneSource,
        type: RingtoneType = RingtoneType.CALL,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        ringtoneManager.setSystemRingtone(
            source = source,
            type = type,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    /**
     * Sets a ringtone for a specific contact.
     *
     * @param source The source of the ringtone (e.g., local file, URL, or resource).
     * @param contact The contact to which the ringtone will be assigned. Use [ContactIdentifier] for ID or phone.
     * @param onSuccess Callback invoked if the ringtone is set successfully.
     * @param onError Callback invoked with a [Throwable] if an error occurs during the operation.
     */
    fun setContactRingtone(
        source: RingtoneSource,
        contact: ContactIdentifier,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        ringtoneManager.setContactRingtone(
            source = source,
            contact = contact,
            onSuccess = onSuccess,
            onError = onError,
        )
    }


    /**
     * Applies a ringtone to a given target (either system or contact).
     *
     * @param source The source of the ringtone.
     * @param target The target to apply the ringtone to, either system or specific contact.
     * @param onSuccess Callback invoked if the ringtone is applied successfully.
     * @param onError Callback invoked with a [Throwable] if an error occurs during the operation.
     */
    fun applyToTarget(
        source: RingtoneSource,
        target: RingtoneTarget,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        ringtoneManager.applyToTarget(source, target, onSuccess, onError)
    }
}
