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

package com.studio.composedemo.ui.navigation

import com.studio.composedemo.model.AssetAudioItem
import kotlinx.serialization.Serializable

sealed interface Screens {

    @Serializable
    object Home : Screens

    @Serializable
    object AssetSystem : Screens

    @Serializable
    object StorageSystem : Screens

    @Serializable
    object AssetsContact : Screens

    @Serializable
    object StorageContact : Screens

    @Serializable
    data class CustomizeDialog(val title:String,val path:String) : Screens{
        fun toAssetAudioItem(): AssetAudioItem {
            return AssetAudioItem(
                title = title,
                path = path
            )
        }
    }
}