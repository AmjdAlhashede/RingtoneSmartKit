package io.github.ringtonesmartkit.domain.model

import android.net.Uri

sealed class ContactIdentifier {
    data class ByUri(val uri: Uri) : ContactIdentifier()
    data class ById(val id: Long) : ContactIdentifier()
    data class ByPhone(val phone: String) : ContactIdentifier()
    object Interactive : ContactIdentifier()
}
