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


import io.github.ringtonesmartkit.data.model.RingtoneContactParams
import io.github.ringtonesmartkit.data.model.RingtoneSystemParams
import io.github.ringtonesmartkit.domain.model.ContactInfo
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.repository.RingtoneManager
import io.github.ringtonesmartkit.domain.repository.RingtoneResultHandler
import io.github.ringtonesmartkit.domain.strategy.applier.RingtoneTargetApplyHandler
import io.github.ringtonesmartkit.domain.strategy.ringtoneloader.RingtoneLoaderHandler
import io.github.ringtonesmartkit.domain.model.ContactTarget
import io.github.ringtonesmartkit.domain.model.SystemTarget
import javax.inject.Inject

internal class RingtoneManagerImpl @Inject constructor(
    private val ringtoneLoadHandler: RingtoneLoaderHandler,
    private val systemHandler: RingtoneTargetApplyHandler<RingtoneSystemParams, Boolean>,
    private val contactHandler: RingtoneTargetApplyHandler<RingtoneContactParams, ContactInfo?>,
) : RingtoneManager {

    override fun setSystemRingtone(
        source: RingtoneSource,
        target: SystemTarget,
    ): RingtoneResultHandler<Unit> {
        return RingtoneResultHandler {
            val insertedUri = ringtoneLoadHandler.run(
                source = source, ringtoneTarget = target
            )
            val param = RingtoneSystemParams(insertedUri = insertedUri.toString())
            systemHandler.apply(param, target)
        }
    }

    override fun setContactRingtone(
        source: RingtoneSource,
        target: ContactTarget,
    ): RingtoneResultHandler<ContactInfo> {
        return RingtoneResultHandler {
            val insertedUri = ringtoneLoadHandler.run(
                source = source, ringtoneTarget = target
            )
            val param = RingtoneContactParams(insertedUri = insertedUri.toString(), target = target)
            val result = contactHandler.apply(param, target)
            result ?: error("there error in setContactRingtone")
        }
    }
}
