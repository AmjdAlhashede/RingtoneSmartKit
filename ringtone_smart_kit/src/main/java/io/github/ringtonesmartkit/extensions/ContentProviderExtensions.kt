package io.github.ringtonesmartkit.extensions

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import io.github.ringtonesmartkit.domain.model.ContactIdentifier
import io.github.ringtonesmartkit.viewmodules.ContactPickerViewModel


internal fun Context.getMimeType(uri: Uri): String? {
    return contentResolver.getType(uri)
}


internal fun ContentValues.finalizePending(context: Context, uri: Uri) {
    clear()
    put(MediaStore.Audio.Media.IS_PENDING, 0)
    context.contentResolver.update(uri, this, null, null)
}

internal suspend fun getContactUriFromIdentifier(
    myViewModel: ContactPickerViewModel,
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
            val result = myViewModel.pickContact()
            printString(result)
            result
        }
    }
}