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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.ringtonesmartkit.api.RingtoneHelper
import io.github.ringtonesmartkit.domain.model.ContactTarget
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import androidx.core.net.toUri

@Composable
fun StorageContactScreen() {
    val context = LocalContext.current

    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedContactTarget by remember { mutableStateOf<ContactTarget>(ContactTarget.Interactive) }
    var inputValue by remember { mutableStateOf("") }
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
        Text("Set Contact Ringtone from Storage", style = MaterialTheme.typography.headlineSmall)

        Button(onClick = { pickAudioLauncher.launch("audio/*") }) {
            Text("Pick Audio File")
        }

        selectedUri?.let {
            Text("Selected: ${it.lastPathSegment ?: "Unknown"}")
        }

        Text("Select Contact Target")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                ContactTarget.Interactive,
                ContactTarget.ByPhone(""),
                ContactTarget.ById(0L),
                ContactTarget.ByUri(Uri.EMPTY)
            ).forEach { target ->
                val label = when (target) {
                    is ContactTarget.Interactive -> "Interactive"
                    is ContactTarget.ByPhone -> "By Phone"
                    is ContactTarget.ById -> "By ID"
                    is ContactTarget.ByUri -> "By URI"
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = target::class == selectedContactTarget::class,
                        onClick = { selectedContactTarget = target }
                    )
                    Text(label)
                }
            }
        }

        // Input field conditionally shown
        when (selectedContactTarget) {
            is ContactTarget.ByPhone -> {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Enter phone number") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is ContactTarget.ById -> {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Enter contact ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is ContactTarget.ByUri -> {
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Enter contact URI") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            else -> { /* no input needed for Interactive */ }
        }

        Button(
            onClick = {
                selectedUri?.let { uri ->
                    isProcessing = true

                    val contactTargetFinal = when (selectedContactTarget) {
                        is ContactTarget.ByPhone -> ContactTarget.ByPhone(inputValue)
                        is ContactTarget.ById -> ContactTarget.ById(inputValue.toLongOrNull() ?: 0L)
                        is ContactTarget.ByUri -> ContactTarget.ByUri(inputValue.toUri())
                        else -> ContactTarget.Interactive
                    }

                    RingtoneHelper.setContactRingtone(
                        source = RingtoneSource.FromStorage(uri),
                        target = contactTargetFinal
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
