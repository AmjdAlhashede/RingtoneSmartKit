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

package io.github.ringtonesmartkit.extensions

import android.content.Context
import java.io.InputStream


const val ASSETS_DIRECTORY = "assets"
const val ASSETS_PATH_SEPARATOR = "/"
const val ASSETS_URI_DIR = "file:///android_assets"

internal fun getAssetsPathSaverToDealWithClassLoader(path: String): String {
    return when {
        path.startsWith(ASSETS_URI_DIR) ->
            path.replace(ASSETS_URI_DIR, ASSETS_DIRECTORY)
        path.startsWith(ASSETS_DIRECTORY) ->
            path
        else ->
            "$ASSETS_DIRECTORY$ASSETS_PATH_SEPARATOR$path"
    }
}

internal fun Context.extractInputStreamFromAssets(path: String): InputStream? {
    return classLoader.getResourceAsStream(getAssetsPathSaverToDealWithClassLoader(path))
}