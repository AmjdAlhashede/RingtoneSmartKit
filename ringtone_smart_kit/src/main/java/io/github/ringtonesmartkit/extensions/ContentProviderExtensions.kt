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


internal fun Context.getMimeType(uri: Uri): String? {
    return contentResolver.getType(uri)
}


internal fun ContentValues.finalizePending(context: Context, uri: Uri) {
    clear()
    put(MediaStore.Audio.Media.IS_PENDING, 0)
    context.contentResolver.update(uri, this, null, null)
}

internal suspend fun getContactUriFromIdentifier(
    identifier: ContactIdentifier,
): Uri? {
    return when (identifier) {
        is ContactIdentifier.ByUri -> identifier.uri
        is ContactIdentifier.ById -> Uri.withAppendedPath(
            ContactsContract.Contacts.CONTENT_URI, identifier.id.toString()
        )

        is ContactIdentifier.ByPhone -> {
            null
        }

        ContactIdentifier.Interactive -> {
            val result = pickContact()
            printString(result)
            result
        }
    }
}