package com.github.ringtonesmartkit.extensions

import android.net.Uri
import android.os.Build
import android.provider.MediaStore

internal val ExternalAudioUri: Uri = sdkQAndUp {
    MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
} ?: MediaStore.Audio.Media.EXTERNAL_CONTENT_URI


internal inline fun <reified T> sdkQAndUp(block: () -> T): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        block()
    } else null
}



