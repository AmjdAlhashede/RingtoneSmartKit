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

package io.github.ringtonesmartkit.utils.extensions

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import io.github.ringtonesmartkit.domain.model.ContactInfo


@Throws(IllegalArgumentException::class)
internal fun getContactInfoFromUri(context: Context, contactUri: Uri): ContactInfo? {
    val contentResolver = context.contentResolver


    val projectionContact = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
    var displayName: String? = null

    contentResolver.query(contactUri, projectionContact, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            if (nameIndex != -1) {
                displayName = cursor.getString(nameIndex)
            }
        }
    }


    val contactId = contactUri.lastPathSegment ?: return null
    val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    val projectionPhone = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
    val selection = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
    val selectionArgs = arrayOf(contactId)

    var phoneNumber: String? = null
    contentResolver.query(phoneUri, projectionPhone, selection, selectionArgs, null)
        ?.use { cursor ->
            if (cursor.moveToFirst()) {
                phoneNumber =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }

    return ContactInfo(contactUri, displayName, phoneNumber)
}


internal fun applyToContact(
    context: Context,
    insertedUri: Uri,
    contactInfo: ContactInfo,
): Boolean {
    val ringtoneValue = ContentValues().apply {
        put(ContactsContract.Contacts.CUSTOM_RINGTONE, insertedUri.toString())
    }

    val rowsUpdated = context.contentResolver.update(
        contactInfo.contactUri, ringtoneValue, null, null
    )

    return rowsUpdated > 0
}