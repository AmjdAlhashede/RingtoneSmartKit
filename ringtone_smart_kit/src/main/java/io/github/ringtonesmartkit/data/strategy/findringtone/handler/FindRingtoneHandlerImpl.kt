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

package io.github.ringtonesmartkit.data.strategy.findringtone.handler

import io.github.ringtonesmartkit.data.strategy.findringtone.factory.FindRingtoneFactory
import io.github.ringtonesmartkit.domain.model.RingtoneMetadata
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.strategy.findringotne.FindRingtoneHandler
import javax.inject.Inject

internal class FindRingtoneHandlerImpl @Inject constructor(
    private val findRingtoneFactory: FindRingtoneFactory,
) : FindRingtoneHandler {

    override suspend fun find(ringtoneSource: RingtoneSource): RingtoneMetadata? {
        return findRingtoneFactory.findThis(ringtoneSource = ringtoneSource)
    }
}