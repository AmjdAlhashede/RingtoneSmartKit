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

package com.studio.composedemo.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

typealias PermissionCallback = (granted: Boolean, permanentlyDenied: Boolean) -> Unit

object PermissionHandler {
    private const val REQUEST_CODE_BASE = 9000
    private var requestCodeCounter = REQUEST_CODE_BASE
    private val callbacks = mutableMapOf<Int, PermissionCallback>()

    fun requestPermissions(
        activity: Activity,
        permissions: Array<String>,
        callback: PermissionCallback
    ) {
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isEmpty()) {
            callback(true, false)
            return
        }

        val requestCode = requestCodeCounter++
        callbacks[requestCode] = callback

        ActivityCompat.requestPermissions(activity, notGranted.toTypedArray(), requestCode)
    }


    fun requestWriteSettingsPermission(context: Context, onResult: (granted: Boolean) -> Unit) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (Settings.System.canWrite(context)) {
                onResult(true)
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                    data = "package:${context.packageName}".toUri()
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                onResult(false)
            }
        }
    }

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun getStoragePermissions(): Array<String> {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
                android.Manifest.permission.READ_MEDIA_AUDIO,
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            else -> arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }


    fun handlePermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        activity: Activity
    ) {
        val callback = callbacks.remove(requestCode) ?: return

        if (grantResults.isEmpty()) {
            callback(false, false)
            return
        }

        val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        val permanentlyDenied = permissions.any { permission ->
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission).not()
                    && ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        }

        callback(allGranted, permanentlyDenied)
    }


}
