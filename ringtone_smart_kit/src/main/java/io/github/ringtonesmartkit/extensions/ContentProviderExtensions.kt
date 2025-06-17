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

package io.github.ringtonesmartkit.extensions

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import io.github.ringtonesmartkit.contract.pickContact
import io.github.ringtonesmartkit.domain.model.ContactIdentifier
import io.github.ringtonesmartkit.domain.model.ContactInfo


internal fun Context.getMimeType(uri: Uri): String? {
    return contentResolver.getType(uri)
}


internal fun ContentValues.finalizePending(context: Context, uri: Uri) {
    clear()
    put(MediaStore.Audio.Media.IS_PENDING, 0)
    context.contentResolver.update(uri, this, null, null)
}


//internal suspend fun getContactUriFromIdentifier(
//    context: Context,
//    identifier: ContactIdentifier,
//): ContactInfo? {
//    return when (identifier) {
//        is ContactIdentifier.ByUri -> {
//            identifier.uri
//        }
//        is ContactIdentifier.ById -> {
//            Uri.withAppendedPath(
//                ContactsContract.Contacts.CONTENT_URI, identifier.id.toString()
//            )
//        }
//
//        is ContactIdentifier.ByPhone -> {
//            val phoneNumber = identifier.phone
//            val contentResolver = context.contentResolver
//
//            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
//            val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
//            val selection = "${ContactsContract.CommonDataKinds.Phone.NUMBER} = ?"
//            val selectionArgs = arrayOf(phoneNumber)
//
//            var contactUri: Uri? = null
//
//            val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
//            cursor?.use {
//                if (it.moveToFirst()) {
//                    val contactId = it.getLong(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
//                    contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId.toString())
//                }
//            }
//            contactUri
//        }
//
//        ContactIdentifier.Interactive -> {
//            val result = pickContact()
//            printString(result)
//            result
//        }
//    }
//}

internal suspend fun getContactInfoFromIdentifier(
    context: Context,
    identifier: ContactIdentifier,
): ContactInfo? {
    val contactUri: Uri? = when (identifier) {
        is ContactIdentifier.ByUri -> identifier.uri

        is ContactIdentifier.ById -> Uri.withAppendedPath(
            ContactsContract.Contacts.CONTENT_URI, identifier.id.toString()
        )

        is ContactIdentifier.ByPhone -> {
            val phoneNumber = identifier.phone
            val contentResolver = context.contentResolver

            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val selection = "${ContactsContract.CommonDataKinds.Phone.NUMBER} = ?"
            val selectionArgs = arrayOf(phoneNumber)

            var contactUri: Uri? = null

            val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val contactId =
                        it.getLong(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                    contactUri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_URI,
                        contactId.toString()
                    )
                }
            }
            contactUri
        }

        ContactIdentifier.Interactive -> {
            pickContact()
        }
    }

    return if (contactUri != null) getContactInfoFromUri(context, contactUri) else null
}


@Throws(IllegalArgumentException::class)
internal suspend fun getContactInfoFromUri(context: Context, contactUri: Uri): ContactInfo? {
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