package io.github.ringtonesmartkit.data.source

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import io.github.ringtonesmartkit.domain.applier.RingtoneAssetsApplier
import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.repository.RingtoneDataSource
import io.github.ringtonesmartkit.extensions.ExternalAudioUri
import io.github.ringtonesmartkit.extensions.extractInputStreamFromAssets
import io.github.ringtonesmartkit.extensions.getFileProviderUri
import io.github.ringtonesmartkit.extensions.nameOfPath
import io.github.ringtonesmartkit.extensions.titleOfPath
import java.io.File

internal class AssetRingtoneDataSource(
    private val context: Context,
    private val ringtoneAssetsApplier: RingtoneAssetsApplier,
) : RingtoneDataSource {

    override suspend fun getSourceType(): RingtoneSource {
        return RingtoneSource.FromAssets("")
    }

    override fun canHandle(source: RingtoneSource): Boolean {
        return source is RingtoneSource.FromAssets
    }

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
    override suspend fun loadRingtone(source: RingtoneSource): RingtoneData {
        val assetSource = source as? RingtoneSource.FromAssets
            ?: throw IllegalArgumentException("Invalid source for AssetRingtoneDataSource")

        val filePath = assetSource.filePath
        val inputStream = context.extractInputStreamFromAssets(filePath)
        val outFile = File(context.cacheDir, filePath.nameOfPath)

        val outUri = context.getFileProviderUri(outFile)
        context.contentResolver.openOutputStream(outUri)?.use { output ->
            inputStream?.copyTo(output)
        }

        return RingtoneData(contentUri = outUri, title = filePath.titleOfPath)
    }

    override suspend fun applyRingtones(
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
        source: RingtoneSource,
    ) {
        ringtoneAssetsApplier.applyAssetsRingtone(
            source=source,
            target = target,
            ringtone = ringtone
        )
    }

    override suspend fun applyRingtonesToContact(
        source: RingtoneSource,
        target: RingtoneTarget.ContactTarget,
        data: RingtoneData,
    ) {
        ringtoneAssetsApplier.applyAssetsContact(
            source =source,
            target = target,
            ringtone = data
        )
    }
}