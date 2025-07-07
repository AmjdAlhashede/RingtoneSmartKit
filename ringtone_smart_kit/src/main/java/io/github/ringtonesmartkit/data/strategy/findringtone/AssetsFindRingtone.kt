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

package io.github.ringtonesmartkit.data.strategy.findringtone

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.core.net.toUri
import io.github.ringtonesmartkit.domain.model.RingtoneMetadata
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.strategy.findringotne.FindRingtone
import io.github.ringtonesmartkit.utils.extensions.ExternalAudioUri
import io.github.ringtonesmartkit.utils.extensions.nameOfPath
import io.github.ringtonesmartkit.utils.extensions.toNormalizedAssetPath
import javax.inject.Inject

internal class AssetsFindRingtone @Inject constructor(
    private val context: Context,
) : FindRingtone {
    override fun canHandler(ringtoneSource: RingtoneSource): Boolean {
        return ringtoneSource is RingtoneSource.FromAssets
    }

    override suspend fun find(ringtoneSource: RingtoneSource): RingtoneMetadata? {
        val assetSource = ringtoneSource as? RingtoneSource.FromAssets
            ?: throw IllegalArgumentException("this is not from assets source")
        val filename = assetSource.filePath.nameOfPath
        val projection = arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DISPLAY_NAME)
        val selection = "${MediaStore.MediaColumns.DATA} LIKE ?"
        val selectionArgs = arrayOf("%$filename")

        context.contentResolver.query(
            ExternalAudioUri,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val displayName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                return RingtoneMetadata(
                    contentUri = ContentUris.withAppendedId(
                        ExternalAudioUri,
                        id
                    ),
                    title = displayName
                )
            }
        }

        return RingtoneMetadata(contentUri = assetSource.filePath.toNormalizedAssetPath().toUri(), title = filename)
    }
}