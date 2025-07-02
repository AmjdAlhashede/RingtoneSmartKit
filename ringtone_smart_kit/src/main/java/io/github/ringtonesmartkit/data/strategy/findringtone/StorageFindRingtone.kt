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

import android.content.Context
import android.provider.MediaStore
import io.github.ringtonesmartkit.domain.model.RingtoneMetadata
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.strategy.findringotne.FindRingtone
import javax.inject.Inject

internal class StorageFindRingtone @Inject constructor(
    private val context: Context,
) : FindRingtone {
    override fun canHandler(ringtoneSource: RingtoneSource): Boolean {
        return ringtoneSource is RingtoneSource.FromStorage
    }

    override suspend fun find(ringtoneSource: RingtoneSource): RingtoneMetadata? {
        val storageSource = ringtoneSource as? RingtoneSource.FromStorage
            ?: throw IllegalArgumentException("Invalid source type for StorageRingtoneDataSource")
        val uri = storageSource.uri
        val displayName = MediaStore.MediaColumns.DISPLAY_NAME
        context.contentResolver.query(
            uri,
            arrayOf(displayName),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameValue = cursor.getString(cursor.getColumnIndexOrThrow(displayName))
                return RingtoneMetadata(
                    contentUri = uri,
                    title = displayNameValue
                )
            }
        }
        return null
    }
}