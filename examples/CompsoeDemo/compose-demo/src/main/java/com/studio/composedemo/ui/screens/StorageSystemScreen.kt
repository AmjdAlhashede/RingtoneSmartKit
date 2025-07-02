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

package com.studio.composedemo.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.ringtonesmartkit.api.RingtoneHelper
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.SystemTarget

@Composable
fun StorageSystemScreen() {
    val context = LocalContext.current

    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedTarget by remember { mutableStateOf<SystemTarget>(SystemTarget.Call) }
    var isProcessing by remember { mutableStateOf(false) }

    val pickAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUri = uri
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Set System Ringtone from Storage", style = MaterialTheme.typography.headlineSmall)

        Button(onClick = { pickAudioLauncher.launch("audio/*") }) {
            Text("Pick Audio File")
        }

        selectedUri?.let {
            Text("Selected: ${it.lastPathSegment ?: "Unknown"}")
        }

        Text("Select Target Type")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(SystemTarget.Call, SystemTarget.Notification, SystemTarget.Alarm).forEach { target ->
                val isSelected = target == selectedTarget
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedTarget = target },
                    label = { Text(target.toString()) }
                )
            }
        }

        Button(
            onClick = {
                selectedUri?.let { uri ->
                    isProcessing = true
                    RingtoneHelper.setSystemRingtone(
                        source = RingtoneSource.FromStorage(uri),
                        target = selectedTarget
                    )
                    .onSuccess {
                        Toast.makeText(context, "Ringtone set successfully", Toast.LENGTH_SHORT).show()
                    }
                    .onFailure {
                        Toast.makeText(context, "Failed: $it", Toast.LENGTH_LONG).show()
                    }
                    .onDone {
                        isProcessing = false
                    }
                    .launch()
                }
            },
            enabled = !isProcessing && selectedUri != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isProcessing) "Processing..." else "Set Ringtone")
        }
    }
}
