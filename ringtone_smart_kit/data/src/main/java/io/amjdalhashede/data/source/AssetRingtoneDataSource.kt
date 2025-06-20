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

package io.amjdalhashede.data.source

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import io.amjdalhashede.data.extensions.ExternalAudioUri
import io.amjdalhashede.data.extensions.extractInputStreamFromAssets
import io.amjdalhashede.data.extensions.nameOfPath
import io.amjdalhashede.data.extensions.titleOfPath
import io.domain.applier.RingtoneAssetsApplier
import io.domain.model.ContactInfo
import io.domain.model.RingtoneData
import io.domain.model.RingtoneSource
import io.domain.model.RingtoneTarget
import io.domain.repository.RingtoneDataSource
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AssetRingtoneDataSource @Inject constructor(
    private val context: Context,
    private val ringtoneAssetsApplier: RingtoneAssetsApplier,
) : RingtoneDataSource {


    override suspend fun getSourceType(): RingtoneSource {
        return RingtoneSource.FromAssets("")
    }

    override fun canHandle(source: RingtoneSource): Boolean {
        return source is RingtoneSource.FromAssets
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun findExistingRingtoneUri(source: RingtoneSource): RingtoneData? {
        source as? RingtoneSource.FromAssets
            ?: throw IllegalArgumentException("this is not from assets source")
        val projection = arrayOf(MediaStore.Audio.Media._ID)
        val selection = "${MediaStore.Audio.Media.DISPLAY_NAME} = ?"

        val selectionArgs = arrayOf(source.filePath)

        context.contentResolver.query(
            ExternalAudioUri,
            projection, selection, selectionArgs, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(0)
                ContentUris.withAppendedId(ExternalAudioUri, id)
            } else null
        }

        return null
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun loadRingtone(source: RingtoneSource): RingtoneData? {
        val assetSource = source as? RingtoneSource.FromAssets
            ?: throw IllegalArgumentException("Invalid source for AssetRingtoneDataSource")

        val filePath = assetSource.filePath
        val inputStream = context.extractInputStreamFromAssets(filePath) ?: throw IllegalArgumentException("Invalid ringtone Maybe Not Found In Assets")
        val outputDir =assetSource.outputDirPath.ifEmpty { context.cacheDir.path }

        val outFile = File(outputDir, filePath.nameOfPath)


        FileOutputStream(outFile).use { output ->
            inputStream.copyTo(output)
        }

        return RingtoneData(contentUri = Uri.fromFile(outFile), title = filePath.titleOfPath)
    }

    override suspend fun applyRingtones(
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
        source: RingtoneSource,
    ) {
        ringtoneAssetsApplier.applyAssetsRingtone(
            source = source,
            target = target,
            ringtone = ringtone
        )
    }

    override suspend fun applyRingtonesToContact(
        source: RingtoneSource,
        target: RingtoneTarget.ContactTarget,
        data: RingtoneData,
    ): ContactInfo? {
        return ringtoneAssetsApplier.applyAssetsContact(
            source = source,
            target = target,
            ringtone = data
        )
    }
}