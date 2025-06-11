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