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

package io.github.ringtonesmartkit.data.source

import android.net.Uri
import io.github.ringtonesmartkit.domain.applier.RingtoneStorageApplier
import io.github.ringtonesmartkit.domain.model.ContactInfo
import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.repository.RingtoneDataSource
import javax.inject.Inject

class StorageRingtoneDataSource @Inject constructor(
    private val storageApplier: RingtoneStorageApplier,
) : RingtoneDataSource {


    override suspend fun getSourceType(): RingtoneSource {
        return RingtoneSource.FromStorage(Uri.EMPTY)
    }

    override fun canHandle(source: RingtoneSource): Boolean {
        return source is RingtoneSource.FromStorage
    }


    @Throws(IllegalArgumentException::class)
    override suspend fun findExistingRingtoneUri(source: RingtoneSource): RingtoneData? {
        val storageSource = source as? RingtoneSource.FromStorage
            ?: throw IllegalArgumentException("Invalid source type for StorageRingtoneDataSource")


        // In Future Complete

        return null
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun loadRingtone(source: RingtoneSource): RingtoneData? {
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
    ): ContactInfo? {
        return storageApplier.applyStorageRingtoneContacts(
            target = target,
            ringtone = data
        )
    }

}