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

package io.github.ringtonesmartkit.data.applier

import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.provider.ContactsContract
import io.github.ringtonesmartkit.domain.applier.RingtoneStorageApplier
import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.extensions.getContactUriFromIdentifier
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RingtoneStorageApplierImpl @Inject constructor(
    private val context: Context,
) : RingtoneStorageApplier {

    override suspend fun applyStorageRingtone(
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
    ) {
        when (target) {
            is RingtoneTarget.System.Call -> RingtoneManager.setActualDefaultRingtoneUri(
                context, RingtoneManager.TYPE_RINGTONE, ringtone.contentUri
            )

            is RingtoneTarget.System.Notification -> RingtoneManager.setActualDefaultRingtoneUri(
                context, RingtoneManager.TYPE_NOTIFICATION, ringtone.contentUri
            )

            is RingtoneTarget.System.Alarm -> RingtoneManager.setActualDefaultRingtoneUri(
                context, RingtoneManager.TYPE_ALARM, ringtone.contentUri
            )
        }
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun applyStorageRingtoneContacts(
        target: RingtoneTarget.ContactTarget,
        ringtone: RingtoneData,
    ) {

        when (target) {
            is RingtoneTarget.ContactTarget.Provided -> {
                val contactUri =
                    getContactUriFromIdentifier(target.identifier)
                        ?: throw IllegalArgumentException("Invalid contact identifier")
                val ringtoneValue = ContentValues().apply {
                    put(ContactsContract.Contacts.CUSTOM_RINGTONE, ringtone.contentUri.toString())
                }

                val rowsUpdated =
                    context.contentResolver.update(contactUri, ringtoneValue, null, null)

                if (rowsUpdated <= 0) {
                    throw IllegalStateException("Failed to update contact with ringtone")
                }
            }
        }
    }
}