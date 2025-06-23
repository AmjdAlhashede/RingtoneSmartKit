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


import io.github.ringtonesmartkit.domain.applier.RingtoneApplyResult
import io.github.ringtonesmartkit.domain.applier.RingtoneApplyResultHandler
import io.github.ringtonesmartkit.domain.model.ContactIdentifier
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.model.RingtoneType
import io.github.ringtonesmartkit.domain.repository.RingtoneManager
import io.github.ringtonesmartkit.domain.ringtoneresult.ContactRingtoneResult
import io.github.ringtonesmartkit.domain.ringtoneresult.ContactRingtoneResultHandler
import io.github.ringtonesmartkit.domain.ringtoneresult.SystemRingtoneResult
import io.github.ringtonesmartkit.domain.ringtoneresult.SystemRingtoneResultHandler
import io.github.ringtonesmartkit.domain.usecase.ApplyContactRingtoneUseCase
import io.github.ringtonesmartkit.domain.usecase.ApplyRingtoneUseCase
import io.github.ringtonesmartkit.domain.usecase.LoadRingtoneUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class RingtoneManagerImpl @Inject constructor(
    private val loadRingtone: LoadRingtoneUseCase,
    private val applyRingtone: ApplyRingtoneUseCase,
    private val applyContactRingtoneUseCase: ApplyContactRingtoneUseCase,
) : RingtoneManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    override fun setSystemRingtone(
        source: RingtoneSource,
        type: RingtoneType,
    ): SystemRingtoneResultHandler {
        val target = mapSystemTypeToTarget(type)
        return applyToTarget(source, target) as SystemRingtoneResultHandler
    }


    override fun setContactRingtone(
        source: RingtoneSource,
        contact: ContactIdentifier,
    ): ContactRingtoneResultHandler {
        val target = RingtoneTarget.ContactTarget.Provided(contact)
        return applyToTarget(source, target) as ContactRingtoneResultHandler
    }

    override fun applyToTarget(
        source: RingtoneSource,
        target: RingtoneTarget,
    ): RingtoneApplyResultHandler {
        val handler: RingtoneApplyResultHandler = when (target) {
            is RingtoneTarget.System -> SystemRingtoneResultHandler()
            is RingtoneTarget.ContactTarget -> ContactRingtoneResultHandler()
        }

        scope.launch {
            runCatching {
                loadAndApplyTarget(source, target)
            }.onSuccess { result ->
               handler.invokeSuccess(result)
            }.onFailure { throwable ->
                handler.invokeFailure(throwable)
            }
        }

        return handler
    }

    @Throws(IllegalStateException::class)
    override suspend fun loadAndApplyTarget(
        source: RingtoneSource,
        target: RingtoneTarget,
    ): RingtoneApplyResult {
        val ringtone = loadRingtone(source)
            ?:return when (target) {
                is RingtoneTarget.System -> throw IllegalStateException("Ringtone could not be loaded")
                is RingtoneTarget.ContactTarget -> throw IllegalStateException("Ringtone could not be loaded")
            }

       return when (target) {
            is RingtoneTarget.System -> {
                applyRingtone(source = source, target = target, ringtone = ringtone)
                SystemRingtoneResult.Success
            }

            is RingtoneTarget.ContactTarget -> {
                val info = applyContactRingtoneUseCase(
                    source = source,
                    target = target,
                    ringtone = ringtone
                )

                info?.let(ContactRingtoneResult::Success) ?: ContactRingtoneResult.Failure(
                    IllegalStateException("No contact info returned")
                )
            }
        }
    }

    private fun mapSystemTypeToTarget(type: RingtoneType): RingtoneTarget.System = when (type) {
        RingtoneType.CALL -> RingtoneTarget.System.Call
        RingtoneType.NOTIFICATION -> RingtoneTarget.System.Notification
        RingtoneType.ALARM -> RingtoneTarget.System.Alarm
    }
}