package io.github.ringtonesmartkit.extensions

import android.net.Uri

internal val String.idFromDocumentId: String
    get() = substringAfterLast(":")

internal val String.nameWithoutExtension: String
    get() = substringBeforeLast(".")

internal val String.extension: String
    get() = substringAfterLast('.', "")

internal val String.nameOfPath: String
    get() = substringAfterLast("/")

internal val String.titleOfPath: String
    get() = nameOfPath.nameWithoutExtension

internal val Uri.titleOfUri: String
    get() = lastPathSegment?.titleOfPath ?: ""