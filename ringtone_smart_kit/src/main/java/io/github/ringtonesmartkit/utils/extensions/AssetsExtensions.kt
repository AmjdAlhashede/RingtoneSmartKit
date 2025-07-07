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

package io.github.ringtonesmartkit.utils.extensions

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import io.github.ringtonesmartkit.presentation.lifecycle.ActivityTracker
import java.io.InputStream


internal const val ASSETS_PATH_SEPARATOR = "/"
internal const val ASSET_PATH_SCHEMA = "android_asset"
internal const val ASSETS_URI_SCHEMA = "file:///android_asset"
internal const val ASSETS_SCHEMA = "assets"


internal fun Context.extractInputStreamFromAssets(path: String): InputStream? {
    return runCatching {
        assets.open(extractPathFromAssetUri(path))
    }.getOrNull()
}



internal fun String.isThisAssetsUri(): Boolean {
    return listOf(
        "$ASSETS_URI_SCHEMA$ASSETS_PATH_SEPARATOR",  // file:///android_asset/
        "$ASSET_PATH_SCHEMA$ASSETS_PATH_SEPARATOR",  // android_asset/
        "$ASSETS_SCHEMA$ASSETS_PATH_SEPARATOR"       // assets/
    ).any { prefix -> this.startsWith(prefix) }
}


internal fun extractPathFromAssetUri(uri: String): String {
    return uri
        .removePrefix("$ASSETS_URI_SCHEMA$ASSETS_PATH_SEPARATOR")
        .removePrefix("$ASSET_PATH_SCHEMA$ASSETS_PATH_SEPARATOR")
        .removePrefix("$ASSETS_SCHEMA$ASSETS_PATH_SEPARATOR")
        .removePrefix(ASSETS_PATH_SEPARATOR)
}

internal fun String.toNormalizedAssetPath(): String {
    val raw = extractPathFromAssetUri(this)
    return "$ASSETS_URI_SCHEMA$ASSETS_PATH_SEPARATOR$raw"
}

internal fun Uri.toNormalizedAssetPath(): Uri {
    return toString().toNormalizedAssetPath().toUri()
}
