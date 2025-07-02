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

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.studio.composedemo.ui.component.rememberAssetAudioFiles
import com.studio.composedemo.util.extensions.showToast
import com.studio.composedemo.model.AssetAudioItem
import com.studio.composedemo.util.PermissionHandler
import com.studio.composedemo.util.RINGTONE_ASSET_DIR
import io.github.ringtonesmartkit.api.RingtoneHelper
import io.github.ringtonesmartkit.domain.model.ContactTarget
import io.github.ringtonesmartkit.domain.model.RingtoneSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetsContactScreen() {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val audioFiles = rememberAssetAudioFiles(RINGTONE_ASSET_DIR)

    var selectedAsset by remember { mutableStateOf<AssetAudioItem?>(null) }
    var selectedTarget by remember { mutableStateOf<ContactTarget>(ContactTarget.Interactive) }
    var inputValue by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }


    fun checkHaveContactPermissions(onHave: () -> Unit) {
        activity?.let { activity ->
            PermissionHandler.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
                )
            ) { granted, permanentlyDenied ->
                if (granted) {
                    onHave()
                } else if (permanentlyDenied) {
                    context.showToast("Please enable contact permissions from settings")
                } else {
                    context.showToast("Invalid context")
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Select Asset Sound", style = MaterialTheme.typography.headlineSmall)

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(audioFiles) { asset ->
                Card(
                    onClick = { selectedAsset = asset },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedAsset == asset)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = asset.title,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (selectedAsset == asset) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Selected"
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider()

        Text("Customize Selected Ringtone", style = MaterialTheme.typography.titleMedium)

        if (selectedAsset != null) {
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
                            selected = target::class == selectedTarget::class,
                            onClick = { selectedTarget = target }
                        )
                        Text(label)
                    }
                }

                when (selectedTarget) {
                    is ContactTarget.ByPhone,
                    is ContactTarget.ById,
                    is ContactTarget.ByUri,
                        -> {
                        val label = when (selectedTarget) {
                            is ContactTarget.ByPhone -> "Phone Number"
                            is ContactTarget.ById -> "Contact ID"
                            is ContactTarget.ByUri -> "Contact URI"
                            else -> ""
                        }

                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            label = { Text(label) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    else -> {}
                }

                Button(
                    onClick = {
                        val finalTarget = when (selectedTarget) {
                            is ContactTarget.ByPhone -> ContactTarget.ByPhone(inputValue)
                            is ContactTarget.ById -> ContactTarget.ById(
                                inputValue.toLongOrNull() ?: 0L
                            )

                            is ContactTarget.ByUri -> ContactTarget.ByUri(inputValue.toUri())
                            else -> ContactTarget.Interactive
                        }

                        checkHaveContactPermissions {
                            isProcessing = true
                            RingtoneHelper.setContactRingtone(
                                source = RingtoneSource.FromAssets(selectedAsset!!.path),
                                target = finalTarget
                            ).onSuccess { contact ->
                                context.showToast("Ringtone set for ${contact.displayName} ")
                                selectedAsset = null
                                inputValue = ""
                                selectedTarget = ContactTarget.Interactive
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
                    enabled = !isProcessing && selectedAsset != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isProcessing) "Processing..." else "Set Contact Ringtone")
                }
            }
        } else {
            Text("Select a sound to customize", style = MaterialTheme.typography.bodySmall)
        }
    }
}

