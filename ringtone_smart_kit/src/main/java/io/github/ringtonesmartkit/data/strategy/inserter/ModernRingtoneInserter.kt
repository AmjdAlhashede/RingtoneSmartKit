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
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import io.github.ringtonesmartkit.data.extensions.ExternalAudioUri
import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneType
import io.github.ringtonesmartkit.domain.strategy.inserter.RingtoneInputStreamProvider
import io.github.ringtonesmartkit.domain.strategy.inserter.RingtoneInserter
import io.github.ringtonesmartkit.domain.strategy.mimetype.MimeTypeExtractor
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.Q)
class ModernRingtoneInserter @Inject constructor(
    private val context: Context,
    private val ringtoneInputStreamProvider: RingtoneInputStreamProvider,
    private val mimeExtractor: MimeTypeExtractor,
) : RingtoneInserter {

    override fun insert(
        ringtone: RingtoneData,
        ringtoneType: RingtoneType,
    ): Uri {
        val mimeType = mimeExtractor.getMimeType(ringtone.contentUri.toString())
        val dir = ringtoneType.getDirectoryName()
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, ringtone.title)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.Audio.Media.RELATIVE_PATH, dir)
            put(MediaStore.Audio.Media.IS_RINGTONE, ringtoneType.isForCall())
            put(MediaStore.Audio.Media.IS_NOTIFICATION, ringtoneType.isForNotification())
            put(MediaStore.Audio.Media.IS_ALARM, ringtoneType.isForAlarm())
            put(MediaStore.Audio.Media.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val insertedUri = resolver.insert(
            ExternalAudioUri,
            values
        ) ?: throw IllegalStateException("Unable to insert into MediaStore in ModernRingtoneInserter")

        resolver.openOutputStream(insertedUri)?.use { output ->
            ringtoneInputStreamProvider.openInputStream(ringtone).copyTo(output)
        }


        val pending = ContentValues().apply { put(MediaStore.Audio.Media.IS_PENDING, 0) }
        resolver.update(insertedUri, pending, null, null)

        return insertedUri
    }
}
