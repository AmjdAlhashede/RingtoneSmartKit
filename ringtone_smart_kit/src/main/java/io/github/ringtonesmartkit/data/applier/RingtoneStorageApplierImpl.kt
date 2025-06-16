package io.github.ringtonesmartkit.data.applier

import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.provider.ContactsContract
import io.github.ringtonesmartkit.viewmodules.ContactPickerViewModel
import io.github.ringtonesmartkit.domain.applier.RingtoneStorageApplier
import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.extensions.getContactUriFromIdentifier

internal class RingtoneStorageApplierImpl(
    private val context: Context,
    private val myViewModel: ContactPickerViewModel,
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
                    getContactUriFromIdentifier(myViewModel = myViewModel, target.identifier)
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