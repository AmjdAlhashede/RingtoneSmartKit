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

package io.github.ringtonesmartkit.data.strategy.ringtoneloader

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import io.github.ringtonesmartkit.domain.model.RingtoneMetadata
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.strategy.inputstreamprovider.RingtoneInputStreamProviderHandler
import io.github.ringtonesmartkit.domain.strategy.mimetype.MimeTypeExtractorHandler
import io.github.ringtonesmartkit.domain.strategy.ringtoneloader.RingtoneLoader
import io.github.ringtonesmartkit.utils.extensions.nameOfUri
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume

private suspend fun scanFileAndGetUri(context: Context, file: File): Uri? =
    suspendCancellableCoroutine { continuation ->
        MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null) { _, uri ->
            if (continuation.isActive) {
                continuation.resume(uri)
            }
        }
    }

internal class LegacyRingtoneLoader @Inject constructor(
    private val context: Context,
    private val ringtoneInputStreamHandler: RingtoneInputStreamProviderHandler,
    private val mimeTypeExtractorHandler: MimeTypeExtractorHandler,
) : RingtoneLoader {
    override suspend fun load(
        ringtoneMetadata: RingtoneMetadata,
        ringtoneTarget: RingtoneTarget,
    ): Uri {
        val mimeType = mimeTypeExtractorHandler.getMimeType(ringtoneMetadata.contentUri.toString())
        val directory = ringtoneTarget.getDirectoryName()

        val targetFile = File(
            Environment.getExternalStoragePublicDirectory(directory),
            ringtoneMetadata.contentUri.nameOfUri
        ).apply {
            parentFile?.mkdirs()
        }

        FileOutputStream(targetFile).use { output ->
            ringtoneInputStreamHandler.openInputStream(ringtoneMetadata = ringtoneMetadata)
                .copyTo(output)
        }

        val insertedUri = scanFileAndGetUri(context, targetFile)?.also { uri ->
            val values = ContentValues().apply {
                put(MediaStore.Audio.Media.IS_RINGTONE, ringtoneTarget.isForCall())
                put(MediaStore.Audio.Media.IS_NOTIFICATION, ringtoneTarget.isForNotification())
                put(MediaStore.Audio.Media.IS_ALARM, ringtoneTarget.isForAlarm())
                put(
                    MediaStore.MediaColumns.MIME_TYPE,
                    mimeType
                )
            }
            context.contentResolver.update(uri, values, null, null)
        }

        if (insertedUri == null) {
            val projection = arrayOf(MediaStore.MediaColumns._ID)
            val selection = "${MediaStore.MediaColumns.DATA} = ?"
            val selectionArgs = arrayOf(targetFile.absolutePath)
            context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    return Uri.withAppendedPath(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id.toString()
                    )
                }
            }
        }

        return insertedUri
            ?: error("Unable to insert/scan file into MediaStore effectively in LegacyRingtoneInserter.")
    }
}
