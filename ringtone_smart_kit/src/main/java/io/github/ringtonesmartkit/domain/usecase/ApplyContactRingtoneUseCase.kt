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

package io.github.ringtonesmartkit.domain.usecase

import io.github.ringtonesmartkit.domain.model.ContactInfo
import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.repository.RingtoneRepository
import javax.inject.Inject

class ApplyContactRingtoneUseCase @Inject constructor(
    private val ringtoneRepository: RingtoneRepository,
) {
    suspend operator fun invoke(
        source: RingtoneSource,
        target: RingtoneTarget.ContactTarget,
        ringtone: RingtoneData,
    ): ContactInfo? {
        return ringtoneRepository.applyContactRingtone(source, target, ringtone)
    }
}
