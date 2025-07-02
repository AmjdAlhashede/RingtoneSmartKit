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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studio.composedemo.ui.component.rememberAssetAudioFiles
import com.studio.composedemo.model.AssetAudioItem
import com.studio.composedemo.ui.navigation.Screens
import com.studio.composedemo.util.ExoPlayerSingleton
import com.studio.composedemo.util.RINGTONE_ASSET_DIR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetsSystemScreen(
    onNavigation: (Screens) -> Unit
) {
    val audioFiles = rememberAssetAudioFiles(RINGTONE_ASSET_DIR)

    LaunchedEffect(Unit) {
        ExoPlayerSingleton.setItems(audioFiles)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(audioFiles) { asset ->
            AssetAudioItemRow(
                item = asset,
                onCustomize = {
                    onNavigation(asset.toCustomizeDialog())
                }
            )
        }
    }
}


@Composable
fun AssetAudioItemRow(
    item: AssetAudioItem,
    onCustomize: () -> Unit,
) {
    Card(onClick = onCustomize) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )

            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "Customize",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
