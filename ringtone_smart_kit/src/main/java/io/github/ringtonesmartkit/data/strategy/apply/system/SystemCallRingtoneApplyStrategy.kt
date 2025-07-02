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

package io.github.ringtonesmartkit.data.strategy.apply.system

import android.content.Context
import android.media.RingtoneManager
import androidx.core.net.toUri
import io.github.ringtonesmartkit.data.model.RingtoneSystemParams
import io.github.ringtonesmartkit.domain.strategy.applier.RingtoneApplyStrategy
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.model.SystemTarget
import javax.inject.Inject


internal class SystemCallRingtoneApplyStrategy @Inject constructor(
    private val context: Context,
) : RingtoneApplyStrategy<RingtoneSystemParams, Boolean> {

    override fun canHandler(target: RingtoneTarget): Boolean {
        return target is SystemTarget.Call
    }

    override suspend fun apply(param: RingtoneSystemParams): Boolean {
        val uri = param.insertedUri.toUri()
        val type = RingtoneManager.TYPE_RINGTONE
        RingtoneManager.setActualDefaultRingtoneUri(
            context, type, uri
        )

        return RingtoneManager.getActualDefaultRingtoneUri(
            context, type
        ) == uri
    }
}

