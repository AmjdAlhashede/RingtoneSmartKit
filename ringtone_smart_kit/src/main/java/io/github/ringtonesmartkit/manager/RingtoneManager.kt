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

package io.github.ringtonesmartkit.manager

import io.github.ringtonesmartkit.api.RingtoneSmartKit
import io.github.ringtonesmartkit.data.ringtoneresult.ContactRingtoneResultHandler
import io.github.ringtonesmartkit.domain.applier.RingtoneApplyResultHandler
import io.github.ringtonesmartkit.data.ringtoneresult.SystemRingtoneResultHandler
import io.github.ringtonesmartkit.domain.model.ContactIdentifier
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.model.RingtoneType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RingtoneManager @Inject constructor(
    private val ringtoneSmartKit: RingtoneSmartKit,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun setSystemRingtone(
        source: RingtoneSource,
        type: RingtoneType = RingtoneType.CALL,
    ): SystemRingtoneResultHandler {
        val target = mapSystemTypeToTarget(type)
        return applyToTarget(source, target) as SystemRingtoneResultHandler
    }

    fun setContactRingtone(
        source: RingtoneSource,
        contact: ContactIdentifier,
    ): ContactRingtoneResultHandler {
        val target = RingtoneTarget.ContactTarget.Provided(contact)
        return applyToTarget(source, target) as ContactRingtoneResultHandler
    }

    fun applyToTarget(
        source: RingtoneSource,
        target: RingtoneTarget,
    ): RingtoneApplyResultHandler {

        val handler: RingtoneApplyResultHandler = when(target) {
            is RingtoneTarget.System -> SystemRingtoneResultHandler()
            is RingtoneTarget.ContactTarget -> ContactRingtoneResultHandler()
        }

        scope.launch {
            runCatching {
                ringtoneSmartKit.loadAndApplyTarget(source, target)
            }.onSuccess { result ->
                handler.invokeSuccess(result)
            }.onFailure { throwable ->
                handler.invokeFailure(throwable)
            }
        }

        return handler
    }

    private fun mapSystemTypeToTarget(type: RingtoneType): RingtoneTarget.System = when (type) {
        RingtoneType.CALL -> RingtoneTarget.System.Call
        RingtoneType.NOTIFICATION -> RingtoneTarget.System.Notification
        RingtoneType.ALARM -> RingtoneTarget.System.Alarm
    }
}
