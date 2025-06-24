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

package io.github.ringtonesmartkit.data.strategy.inserter

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import io.github.ringtonesmartkit.data.extensions.nameOfUri
import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneType
import io.github.ringtonesmartkit.domain.strategy.inserter.RingtoneInputStreamProvider
import io.github.ringtonesmartkit.domain.strategy.inserter.RingtoneInserter
import io.github.ringtonesmartkit.domain.strategy.mimetype.MimeTypeExtractor
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class LegacyRingtoneInserter @Inject constructor(
    private val context: Context,
    private val ringtoneInputStreamProvider: RingtoneInputStreamProvider,
    private val mimeTypeExtractor: MimeTypeExtractor,
) : RingtoneInserter {


    override fun insert(
        ringtone: RingtoneData,
        ringtoneType: RingtoneType,
    ): Uri {
        val directory = ringtoneType.getDirectoryName()

        val targetFile = File(
            Environment.getExternalStoragePublicDirectory(directory),
            ringtone.contentUri.nameOfUri
        ).apply {
            parentFile?.mkdirs()
        }

        FileOutputStream(targetFile).use { output ->
            ringtoneInputStreamProvider.openInputStream(ringtone).copyTo(output)
        }

        var insertedUri: Uri? = null

        MediaScannerConnection.scanFile(
            context,
            arrayOf(targetFile.absolutePath),
            null
        ) { path, uri ->
            uri?.let { uri ->
                insertedUri = uri
                val values = ContentValues().apply {
                    put(MediaStore.Audio.Media.IS_RINGTONE, ringtoneType.isForCall())
                    put(MediaStore.Audio.Media.IS_NOTIFICATION, ringtoneType.isForNotification())
                    put(MediaStore.Audio.Media.IS_ALARM, ringtoneType.isForAlarm())
                    put(
                        MediaStore.MediaColumns.MIME_TYPE,
                        mimeTypeExtractor.getMimeType(ringtone.contentUri.toString())
                    )
                }
                context.contentResolver.update(uri, values, null, null)
            }

        }

        insertedUri ?: run {
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
                    insertedUri = Uri.withAppendedPath(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id.toString()
                    )
                }
            }
        }

        return insertedUri
            ?: throw IllegalStateException("Unable to insert/scan file into MediaStore effectively in LegacyRingtoneInserter.")
    }

}
