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

package io.amjdalhashede.data.applier

import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import io.amjdalhashede.data.extensions.ExternalAudioUri
import io.amjdalhashede.data.extensions.finalizePending
import io.amjdalhashede.data.extensions.getContactInfoFromIdentifier
import io.amjdalhashede.data.extensions.getMimeType
import io.domain.applier.RingtoneAssetsApplier
import io.domain.model.ContactInfo
import io.domain.model.RingtoneData
import io.domain.model.RingtoneSource
import io.domain.model.RingtoneTarget
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RingtoneAssetsApplierImpl @Inject constructor(
    private val context: Context,
) : RingtoneAssetsApplier {


    @Throws(IllegalArgumentException::class)
    override suspend fun applyAssetsRingtone(
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
        source: RingtoneSource,
    ) {

        val insertedUri = insertRingtoneToMediaStore(
            ringtone = ringtone,
            isForCall = target is RingtoneTarget.System.Call,
            isForNotification = target is RingtoneTarget.System.Notification,
            isForAlarm = target is RingtoneTarget.System.Alarm
        )

        when (target) {
            is RingtoneTarget.System.Call -> RingtoneManager.setActualDefaultRingtoneUri(
                context, RingtoneManager.TYPE_RINGTONE, insertedUri
            )

            is RingtoneTarget.System.Notification -> RingtoneManager.setActualDefaultRingtoneUri(
                context, RingtoneManager.TYPE_NOTIFICATION, insertedUri
            )

            is RingtoneTarget.System.Alarm -> RingtoneManager.setActualDefaultRingtoneUri(
                context, RingtoneManager.TYPE_ALARM, insertedUri
            )
        }
    }


    @Throws(IllegalArgumentException::class)
    override suspend fun applyAssetsContact(
        target: RingtoneTarget.ContactTarget,
        ringtone: RingtoneData,
        source: RingtoneSource,
    ): ContactInfo? {
        val insertedUri = insertRingtoneToMediaStore(ringtone = ringtone, isForCall = true)
        return when (
            target
        ) {
            is RingtoneTarget.ContactTarget.Provided -> {
                val contactInfo = getContactInfoFromIdentifier(context = context, target.identifier)
                    ?: throw IllegalArgumentException("Invalid contact identifier")

                val ringtoneValue = ContentValues().apply {
                    put(ContactsContract.Contacts.CUSTOM_RINGTONE, insertedUri.toString())
                }

                val rowsUpdated =
                    context.contentResolver.update(
                        contactInfo.contactUri,
                        ringtoneValue,
                        null,
                        null
                    )

                if (rowsUpdated <= 0) {
                    throw IllegalStateException("Failed to update contact with ringtone")
                }

                contactInfo
            }
        }
    }

    @Throws(IllegalStateException::class)
    private fun insertRingtoneToMediaStore(
        ringtone: RingtoneData,
        isForCall: Boolean = true,
        isForNotification: Boolean = false,
        isForAlarm: Boolean = false,
    ): Uri {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, ringtone.title)
            put(
                MediaStore.MediaColumns.MIME_TYPE,
                context.getMimeType(ringtone.contentUri) ?: "audio/*"
            )
            put(MediaStore.Audio.Media.IS_RINGTONE, isForCall)
            put(MediaStore.Audio.Media.IS_NOTIFICATION, isForNotification)
            put(MediaStore.Audio.Media.IS_ALARM, isForAlarm)
            put(MediaStore.Audio.Media.IS_PENDING, 1)
        }

        val contentResolver = context.contentResolver

        val insertedUri = contentResolver.insert(ExternalAudioUri, values)
            ?: throw IllegalStateException("Unable to insert into MediaStore")

        contentResolver.openOutputStream(insertedUri)?.use { outStream ->
            contentResolver.openInputStream(ringtone.contentUri)?.copyTo(outStream)
        }

        values.finalizePending(context = context, uri = insertedUri)

        return insertedUri
    }


}