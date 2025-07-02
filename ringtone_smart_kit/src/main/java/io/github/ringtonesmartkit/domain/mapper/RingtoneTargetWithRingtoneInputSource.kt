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
import io.github.ringtonesmartkit.domain.model.RingtoneMetadata
import io.github.ringtonesmartkit.domain.types.RingtoneInputType
import io.github.ringtonesmartkit.utils.extensions.isThisAssetsUri

internal fun RingtoneMetadata.toRingtoneInputType(): RingtoneInputType {
    return if (contentUri.toString().isThisAssetsUri()) {
        RingtoneInputType.ASSET_URI
    } else {
        when (this.contentUri.scheme) {
            ContentResolver.SCHEME_CONTENT -> RingtoneInputType.CONTENT_URI
            ContentResolver.SCHEME_FILE -> RingtoneInputType.FILE_URI
            else -> throw IllegalArgumentException("This is Unknown Uri Scheme")
        }
    }
}