package io.github.ringtonesmartkit.domain.model

import android.net.Uri

data class RingtoneData(
    val contentUri: Uri,
    val title: String = ""
)