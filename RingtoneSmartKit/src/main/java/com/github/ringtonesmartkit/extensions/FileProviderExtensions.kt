package com.github.ringtonesmartkit.extensions

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

internal fun Context.getFileProviderUri(file: File): Uri{
    return FileProvider.getUriForFile(
        this,
        "$packageName.provider",
        file
    )
}