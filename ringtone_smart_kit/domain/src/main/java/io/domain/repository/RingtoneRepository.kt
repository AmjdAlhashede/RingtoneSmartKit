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

package io.domain.repository

import io.domain.model.ContactInfo
import io.domain.model.RingtoneData
import io.domain.model.RingtoneSource
import io.domain.model.RingtoneTarget


interface RingtoneRepository {
    suspend fun getRingtone(source: RingtoneSource): RingtoneData?
    suspend fun applyRingtones(
        source: RingtoneSource,
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
    )

    suspend fun applyContactRingtone(
        source: RingtoneSource,
        target: RingtoneTarget.ContactTarget,
        data: RingtoneData,
    ): ContactInfo?
}
