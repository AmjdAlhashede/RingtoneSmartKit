package io.github.ringtonesmartkit.domain.model

import android.net.Uri


sealed class RingtoneSource {

    data class FromAssets(val filePath: String,val outputDirPath: String="") : RingtoneSource()

    data class FromStorage(val uri: Uri) : RingtoneSource()

    data class FromUrl(val url: String) : RingtoneSource()

    override fun toString(): String = when (this) {
        is FromAssets -> "Assets($filePath)"
        is FromStorage -> "Storage(${uri.lastPathSegment})"
        is FromUrl -> "Url($url)"
    }
}
