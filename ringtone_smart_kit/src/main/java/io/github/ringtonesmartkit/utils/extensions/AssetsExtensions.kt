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
internal const val ASSET_PATH_SCHEMA = "android_assets"
internal const val ASSETS_URI_SCHEMA = "file:///android_assets"
internal const val ASSETS_SCHEMA = "assets"

internal fun getAssetsPathSaverToDealWithClassLoader(path: String): String {
    return when {
        path.startsWith(ASSETS_URI_SCHEMA) -> path.replace(ASSETS_URI_SCHEMA, ASSETS_SCHEMA)
        path.startsWith(ASSET_PATH_SCHEMA) -> path.replace(ASSET_PATH_SCHEMA, ASSETS_SCHEMA)
        path.startsWith(ASSETS_SCHEMA) -> path
        else -> "$ASSETS_SCHEMA$ASSETS_PATH_SEPARATOR$path"
    }
}

internal fun extractPathFromAssetUri(uri: String): String {
    val cleaned = when {
        uri.startsWith(ASSETS_URI_SCHEMA) -> uri.removePrefix(ASSETS_URI_SCHEMA)
        uri.startsWith(ASSET_PATH_SCHEMA) -> uri.removePrefix(ASSET_PATH_SCHEMA)
        uri.startsWith(ASSETS_SCHEMA + ASSETS_PATH_SEPARATOR) ->
            uri.removePrefix(ASSETS_SCHEMA + ASSETS_PATH_SEPARATOR)

        else -> uri
    }
    return cleaned.removePrefix(ASSETS_PATH_SEPARATOR)
}

internal fun Context.extractInputStreamFromAssets(path: String): InputStream? {
//    return classLoader.getResourceAsStream(getAssetsPathSaverToDealWithClassLoader(path))
    return runCatching {
        assets.open(extractPathFromAssetUri(path))
    }.getOrNull()
}

internal fun String.toAssetUri(): Uri {
    return "file:///android_assets/$this".toUri()
}

internal fun String.isThisAssetsUri(): Boolean {
    return when {
        this.startsWith(ASSETS_URI_SCHEMA) or this.startsWith(ASSETS_SCHEMA) -> {
            true
        }

        else -> {
            return runCatching {
                ActivityTracker.currentActivity?.assets?.open(this)
                true
            }.getOrElse {
                false
            }
        }
    }
}
