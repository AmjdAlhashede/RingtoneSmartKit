package io.github.ringtonesmartkit.domain.model

import android.net.Uri

internal data class RingtoneData(
    val contentUri: Uri,
    val title: String = ""
)