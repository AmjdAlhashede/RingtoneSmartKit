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

package io.github.ringtonesmartkit.data.strategy.inserter_provider

import android.content.ContentResolver
import android.net.Uri
import io.github.ringtonesmartkit.data.extensions.isThisAssetsUri
import io.github.ringtonesmartkit.domain.strategy.inserter.RingtoneInputStreamProvider
import javax.inject.Inject

class InputStreamProviderFactory @Inject constructor(
    private val contentUriProvider: ContentUriInputStreamProvider,
    private val fileUriProvider: FileUriInputStreamProvider,
    private val assetUriProvider: AssetUriInputStreamProvider,
) {
    fun getProvider(uri: Uri): RingtoneInputStreamProvider = if (uri.toString().isThisAssetsUri()) {
        assetUriProvider
    } else {
        when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> contentUriProvider
            ContentResolver.SCHEME_FILE -> fileUriProvider
            else -> throw IllegalArgumentException("Unsupported URI scheme: ${uri.scheme}")
        }
    }
}
