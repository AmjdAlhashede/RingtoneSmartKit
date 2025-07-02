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

package com.studio.composedemo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studio.composedemo.ui.navigation.Screens

@Composable
fun HomeScreen( onNavigation : (Screens)-> Unit) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "🎵 RingtoneSmartKit Demo",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onNavigation(Screens.AssetSystem)
            },
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Set System Ringtone from Assets")
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onNavigation(Screens.StorageSystem)
            },
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Set System Ringtone from Storage")
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onNavigation(Screens.AssetsContact)
            },
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Set Contact Ringtone from Assets")
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onNavigation(Screens.StorageContact)
            },
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Set Contact Ringtone from Storage")
            }
        }
    }
}
