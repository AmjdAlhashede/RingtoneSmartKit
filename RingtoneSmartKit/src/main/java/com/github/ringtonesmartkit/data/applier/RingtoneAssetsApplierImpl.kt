package com.github.ringtonesmartkit.data.applier

import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import com.github.ringtonesmartkit.viewmodules.ContactPickerViewModel
import com.github.ringtonesmartkit.domain.applier.RingtoneAssetsApplier
import com.github.ringtonesmartkit.domain.model.RingtoneData
import com.github.ringtonesmartkit.domain.model.RingtoneSource
import com.github.ringtonesmartkit.domain.model.RingtoneTarget
import com.github.ringtonesmartkit.extensions.ExternalAudioUri
import com.github.ringtonesmartkit.extensions.finalizePending
import com.github.ringtonesmartkit.extensions.getContactUriFromIdentifier
import com.github.ringtonesmartkit.extensions.getMimeType

internal class RingtoneAssetsApplierImpl(
    private val context: Context,
    private val myViewModel: ContactPickerViewModel,
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
    ) {
        val insertedUri = insertRingtoneToMediaStore(ringtone = ringtone, isForCall = true)
        when (
            target
        ) {
            is RingtoneTarget.ContactTarget.Provided -> {
                val contactUri =
                    getContactUriFromIdentifier(myViewModel = myViewModel, target.identifier)
                        ?: throw IllegalArgumentException("Invalid contact identifier")
                val ringtoneValue = ContentValues().apply {
                    put(ContactsContract.Contacts.CUSTOM_RINGTONE, insertedUri.toString())
                }

                val rowsUpdated =
                    context.contentResolver.update(contactUri, ringtoneValue, null, null)

                if (rowsUpdated <= 0) {
                    throw IllegalStateException("Failed to update contact with ringtone")
                }
            }
        }
    }


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