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
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import io.github.ringtonesmartkit.domain.model.RingtoneMetadata
import io.github.ringtonesmartkit.domain.strategy.inputstreamprovider.RingtoneInputStreamProviderHandler
import io.github.ringtonesmartkit.domain.strategy.mimetype.MimeTypeExtractorHandler
import io.github.ringtonesmartkit.domain.strategy.ringtoneloader.RingtoneLoader
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.utils.extensions.ExternalAudioUri
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.Q)
internal class ModernRingtoneLoader @Inject constructor(
    private val context: Context,
    private val ringtoneInputStreamHandler: RingtoneInputStreamProviderHandler,
    private val mimeTypeExtractorHandler: MimeTypeExtractorHandler,
) : RingtoneLoader {


    override suspend fun load(
        ringtoneMetadata: RingtoneMetadata,
        ringtoneTarget: RingtoneTarget,
    ): Uri {

        val mimeType = mimeTypeExtractorHandler.getMimeType(ringtoneMetadata.contentUri.toString())
        val dir = ringtoneTarget.getDirectoryName()
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, ringtoneMetadata.title)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.Audio.Media.RELATIVE_PATH, dir)
            put(MediaStore.Audio.Media.IS_RINGTONE, ringtoneTarget.isForCall())
            put(MediaStore.Audio.Media.IS_NOTIFICATION, ringtoneTarget.isForNotification())
            put(MediaStore.Audio.Media.IS_ALARM, ringtoneTarget.isForAlarm())
            put(MediaStore.Audio.Media.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val insertedUri = resolver.insert(
            ExternalAudioUri,
            values
        ) ?: throw IllegalStateException("Unable to insert into MediaStore in ModernRingtoneInserter")

        resolver.openOutputStream(insertedUri)?.use { output ->
            ringtoneInputStreamHandler.openInputStream(
                ringtoneMetadata = ringtoneMetadata
            ).copyTo(output)
        }


        val pending = ContentValues().apply { put(MediaStore.Audio.Media.IS_PENDING, 0) }
        resolver.update(insertedUri, pending, null, null)

        return insertedUri
    }
}
