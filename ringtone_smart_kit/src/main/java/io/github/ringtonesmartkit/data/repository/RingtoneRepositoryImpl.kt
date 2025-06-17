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

package io.github.ringtonesmartkit.data.repository

import io.github.ringtonesmartkit.domain.model.ContactInfo
import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.repository.RingtoneDataSource
import io.github.ringtonesmartkit.domain.repository.RingtoneRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class RingtoneRepositoryImpl @Inject constructor(
    private val sources: List<@JvmSuppressWildcards RingtoneDataSource>,
) : RingtoneRepository {

    override suspend fun getAvailableSources(): List<RingtoneSource> = sources.map { it.getSourceType() }

    override suspend fun getRingtone(source: RingtoneSource): RingtoneData? {
        return sources.firstOrNull { it.canHandle(source) }?.loadRingtone(source)
    }

    override suspend fun applyRingtones(
        source: RingtoneSource,
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
    ) {
        sources.firstOrNull { it.canHandle(source) }
            ?.applyRingtones(source = source, target = target, ringtone = ringtone)
    }

    override suspend fun applyContactRingtone(
        source: RingtoneSource,
        target: RingtoneTarget.ContactTarget,
        data: RingtoneData,
    ) : ContactInfo? {
       return sources.firstOrNull { it.canHandle(source) }
            ?.applyRingtonesToContact(
                source = source,
                target = target,
                data = data
            )
    }
}