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

package com.studio.composedemo.ui.dialogs


import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.studio.composedemo.util.extensions.showToast
import com.studio.composedemo.model.AssetAudioItem
import com.studio.composedemo.util.PermissionHandler
import io.github.ringtonesmartkit.api.RingtoneHelper
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.SystemTarget
import androidx.compose.ui.graphics.Color

data class CustomizeDialogData(val title: String, val icon: ImageVector, val onClick: () -> Unit)

@Composable
fun CustomizeDialog(
    asset: AssetAudioItem,
    onDismiss: () -> Unit,
) {

    val context = LocalContext.current

    val activity = LocalActivity.current


    fun checkHaveStoragePermissions(onHave: () -> Unit) {
        activity?.let { activity ->
            PermissionHandler.requestPermissions(
                activity,
                PermissionHandler.getStoragePermissions()
            ) { granted, permanentlyDenied ->
                if (granted) {
                    onHave()
                } else if (permanentlyDenied) {
                    context.showToast("Please enable Storage permissions from settings")
                } else {
                    context.showToast("Invalid context")
                }
            }
        }
    }

    fun setSystem(
        target: SystemTarget,
    ) {
        PermissionHandler.requestWriteSettingsPermission(context) {
            if(it){
                checkHaveStoragePermissions {
                    activity?.let {activity->
                        RingtoneHelper.setSystemRingtone(
                            source = RingtoneSource.FromAssets(asset.path), target = target
                        ).onSuccess {
                            context.showToast("successfully")
                        }.onFailure { context.showToast(it.message.toString()) }.launch()
                    }
                }
            }
        }
    }


    val allItems = remember {
        listOf(
            CustomizeDialogData(
                "Set as Ringtone", icon = Icons.Default.Phone, onClick = {
                    setSystem(SystemTarget.Call)
                }),

            CustomizeDialogData(
                "Set as Notification", icon = Icons.Default.Notifications, onClick = {
                    setSystem(SystemTarget.Notification)
                }),

            CustomizeDialogData(
                "Set as Alarm", icon = Icons.Rounded.Add, onClick = {
                    setSystem(SystemTarget.Alarm)
                }),
        )
    }


    AlertDialog(onDismissRequest = onDismiss, title = { Text("Set as...") }, text = {
        LazyColumn {
            items(allItems) { item ->
                ListItem(
                    modifier = Modifier.clickable{ item.onClick()},
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    ),
                    leadingContent = {
                       Icon(
                            imageVector = item.icon, contentDescription = item.title
                        )
                    },
                    headlineContent = { Text(item.title) },
                    trailingContent = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "this is icon for ${item.icon.name}"
                        )
                    }
                )
            }
        }
    }, confirmButton = {}, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}
