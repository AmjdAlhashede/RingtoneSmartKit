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

package io.github.ringtonesmartkit.api

import io.github.ringtonesmartkit.data.ringtoneresult.ContactRingtoneResult
import io.github.ringtonesmartkit.domain.applier.RingtoneApplyResult
import io.github.ringtonesmartkit.data.ringtoneresult.SystemRingtoneResult
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.usecase.ApplyContactRingtoneUseCase
import io.github.ringtonesmartkit.domain.usecase.ApplyRingtoneUseCase
import io.github.ringtonesmartkit.domain.usecase.LoadRingtoneUseCase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class RingtoneSmartKit @Inject constructor(
    private val loadRingtone: LoadRingtoneUseCase,
    private val applyRingtone: ApplyRingtoneUseCase,
    private val applyContactRingtoneUseCase: ApplyContactRingtoneUseCase,
) {

    suspend fun loadAndApplyTarget(
        source: RingtoneSource,
        target: RingtoneTarget,
    ): RingtoneApplyResult {
        return try {
            val ringtone = loadRingtone(source)
                ?: return when (target) {
                    is RingtoneTarget.System -> SystemRingtoneResult.Failure(
                        IllegalStateException("Ringtone could not be loaded")
                    )

                    is RingtoneTarget.ContactTarget -> ContactRingtoneResult.Failure(
                        IllegalStateException("Ringtone could not be loaded")
                    )
                }

            when (target) {
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
        } catch (e: Throwable) {
            when (target) {
                is RingtoneTarget.System -> SystemRingtoneResult.Failure(e)
                is RingtoneTarget.ContactTarget -> ContactRingtoneResult.Failure(e)
            }
        }
    }
}
