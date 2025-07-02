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
import androidx.core.net.toUri
import io.github.ringtonesmartkit.domain.types.MimeExtractorTypes
import io.github.ringtonesmartkit.utils.extensions.isThisAssetsUri

internal fun String.toMimeTypeExtractorType(): MimeExtractorTypes {
    val lower = this.lowercase()
    return when {
        lower.startsWith("${ContentResolver.SCHEME_CONTENT}://") -> MimeExtractorTypes.CONTENT_URI
        lower.startsWith("${ContentResolver.SCHEME_FILE}://") -> MimeExtractorTypes.FILE_URI
        this.isThisAssetsUri() -> MimeExtractorTypes.ASSET_URI
        else -> error("This is Unknown Uri Scheme to MimeTypeExtractorType + \n $this")
    }
}