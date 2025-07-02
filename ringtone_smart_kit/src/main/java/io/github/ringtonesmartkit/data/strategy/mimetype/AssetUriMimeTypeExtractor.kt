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

package io.github.ringtonesmartkit.data.strategy.mimetype

import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import androidx.core.net.toUri
import io.github.ringtonesmartkit.domain.strategy.mimetype.MimeTypeExtractor
import io.github.ringtonesmartkit.domain.types.MimeExtractorTypes
import io.github.ringtonesmartkit.utils.extensions.nameOfPath
import io.github.ringtonesmartkit.utils.extensions.sdkQAndUp
import java.nio.file.Files
import javax.inject.Inject


internal class AssetUriMimeTypeExtractor @Inject constructor() : MimeTypeExtractor {
    override val typeExtractor: MimeExtractorTypes = MimeExtractorTypes.ASSET_URI
    override suspend fun getMimeType(uri: String): String {
        return MimeTypeMap.getFileExtensionFromUrl(uri.nameOfPath)
            ?.let { MimeTypeMap.getSingleton().getMimeTypeFromExtension(it) }
            ?: sdkQAndUp {
                Files.probeContentType(uri.toUri().toFile().toPath())
            } ?: "application/octet-stream"
    }
}