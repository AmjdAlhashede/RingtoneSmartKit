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

package io.github.ringtonesmartkit.domain.model

import android.net.Uri
import android.os.Environment

sealed interface RingtoneTarget {
    fun getDirectoryName(): String = when (this) {
        SystemTarget.Call -> Environment.DIRECTORY_RINGTONES
        SystemTarget.Notification -> Environment.DIRECTORY_NOTIFICATIONS
        SystemTarget.Alarm -> Environment.DIRECTORY_ALARMS
        is ContactTarget -> Environment.DIRECTORY_RINGTONES
    }

    fun isForCall() = this == SystemTarget.Call
    fun isForNotification() = this == SystemTarget.Notification
    fun isForAlarm() = this == SystemTarget.Alarm
}


sealed class SystemTarget : RingtoneTarget {

    object Call : SystemTarget()

    object Notification : SystemTarget()

    object Alarm : SystemTarget()

}


sealed class ContactTarget : RingtoneTarget {
    data class ByUri(val contactUri: Uri) : ContactTarget()
    data class ById(val id: Long) : ContactTarget()
    data class ByPhone(val phone: String) : ContactTarget()
    object Interactive : ContactTarget()
}