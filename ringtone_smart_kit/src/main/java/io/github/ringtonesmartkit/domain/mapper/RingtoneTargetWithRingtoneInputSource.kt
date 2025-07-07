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

package io.github.ringtonesmartkit.domain.mapper

import android.content.ContentResolver
import android.net.Uri
import io.github.ringtonesmartkit.domain.types.RingtoneInputType
import io.github.ringtonesmartkit.utils.extensions.isThisAssetsUri

internal fun String.toRingtoneInputType(): RingtoneInputType {
    val lower = this.lowercase()
    return when {
        lower.startsWith("${ContentResolver.SCHEME_CONTENT}://") -> RingtoneInputType.CONTENT_URI
        lower.isThisAssetsUri() -> RingtoneInputType.ASSET_URI
        lower.startsWith("${ContentResolver.SCHEME_FILE}://") or lower.startsWith("/") -> RingtoneInputType.FILE_URI
        else -> throw IllegalArgumentException("Unknown Uri Scheme for RingtoneInputType:\n$this")
    }
}

internal fun Uri.toRingtoneInputType(): RingtoneInputType = this.toString().toRingtoneInputType()
