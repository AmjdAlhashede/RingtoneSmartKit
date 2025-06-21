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

package io.domain.applier

import io.domain.model.ContactInfo
import io.domain.model.RingtoneData
import io.domain.model.RingtoneSource
import io.domain.model.RingtoneTarget


interface RingtoneAssetsApplier {
    suspend fun applyAssetsRingtone(
        target: RingtoneTarget.System,
        ringtone: RingtoneData,
        source: RingtoneSource
    )

    suspend fun applyAssetsContact(
        target: RingtoneTarget.ContactTarget,
        ringtone: RingtoneData,
        source: RingtoneSource
    ): ContactInfo?

}