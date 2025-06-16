package io.github.ringtonesmartkit.data.source

import android.content.Context
import android.net.Uri
import io.github.ringtonesmartkit.domain.applier.RingtoneStorageApplier
import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.repository.RingtoneDataSource


internal class StorageRingtoneDataSource(
    private val context: Context,
    private val storageApplier: RingtoneStorageApplier,
) : RingtoneDataSource {

    override suspend fun getSourceType(): RingtoneSource {
        return RingtoneSource.FromStorage(Uri.EMPTY)
    }

    override fun canHandle(source: RingtoneSource): Boolean {
        return source is RingtoneSource.FromStorage
    }

    override suspend fun findExistingRingtoneUri(source: RingtoneSource): RingtoneData? {
        return null
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun loadRingtone(source: RingtoneSource): RingtoneData {
        val storageSource = source as? RingtoneSource.FromStorage
            ?: throw IllegalArgumentException("Invalid source type for StorageRingtoneDataSource")

        return RingtoneData(contentUri = storageSource.uri)
    }

    override suspend fun applyRingtones(
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
        source: RingtoneSource,
    ) {
        storageApplier.applyStorageRingtone(
            target = target,
            ringtone = ringtone
        )
    }

    override suspend fun applyRingtonesToContact(
        source: RingtoneSource,
        target: RingtoneTarget.ContactTarget,
        data: RingtoneData,
    ) {
        storageApplier.applyStorageRingtoneContacts(
            target = target,
            ringtone = data
        )
    }
}