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

import android.content.ContentResolver
import androidx.core.net.toFile
import androidx.core.net.toUri
import io.github.ringtonesmartkit.data.extensions.ASSETS_URI_SCHEMA
import io.github.ringtonesmartkit.data.extensions.isThisAssetsUri
import io.github.ringtonesmartkit.data.extensions.sdkQAndUp
import io.github.ringtonesmartkit.domain.strategy.mimetype.MimeTypeExtractor
import java.nio.file.Files
import javax.inject.Inject

class DefaultMimeTypeExtractor @Inject constructor(
    private val contentUriMimeTypeExtractor: ContentUriMimeTypeExtractor,
    private val fileUriMimeTypeExtractor: FileUriMimeTypeExtractor,
    private val assetUriMimeTypeExtractor: AssetUriMimeTypeExtractor
) : MimeTypeExtractor {

    override fun getMimeType(uri: String): String {
        return if(uri.isThisAssetsUri()){
            assetUriMimeTypeExtractor.getMimeType(uri)
        }else{
            when (uri.toUri().scheme) {
                ContentResolver.SCHEME_CONTENT -> contentUriMimeTypeExtractor.getMimeType(uri)
                ContentResolver.SCHEME_FILE -> fileUriMimeTypeExtractor.getMimeType(uri)
                else -> sdkQAndUp {
                    Files.probeContentType(uri.toUri().toFile().toPath())
                }?: "application/octet-stream"
            }
        }
    }
}
