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

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.studio.composedemo.model.AssetAudioItem

object ExoPlayerSingleton {
    private var player: ExoPlayer? = null
    private val allItems = mutableListOf<AssetAudioItem>()

    fun init(context: Context) {
        if (player == null) {
            player = ExoPlayer.Builder(context.applicationContext).build()
        }
    }

    fun get(): ExoPlayer {
        return player ?: error("ExoPlayer not initialized. Call init(context) first.")
    }

    fun setItems(items: List<AssetAudioItem>) {
        if (allItems != items) {
            allItems.clear()
            allItems.addAll(items)

            get().apply {
                items.map { MediaItem.fromUri("file:///android_asset/${it.path}") }
                    .also { mediaItems ->
                        setMediaItems(mediaItems)
                        prepare()
                        playWhenReady = false
                    }
            }
        }

    }

    fun play(data: AssetAudioItem) {
        val index =
            allItems.indexOfFirst { it == data }.takeIf { it >= 0 } ?: error("this is not found ")
        get().seekTo(index, 0)
        get().playWhenReady = true
    }

    fun release() {
        player?.release()
        player = null
    }
}
