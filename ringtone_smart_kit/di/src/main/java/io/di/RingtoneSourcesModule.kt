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

package io.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import io.amjdalhashede.data.source.AssetRingtoneDataSource
import io.amjdalhashede.data.source.StorageRingtoneDataSource
import io.domain.repository.RingtoneDataSource

@Module
object RingtoneSourcesModule {

    @Provides
    @IntoSet
    fun provideAssetRingtoneDataSource(
        assetRingtoneDataSource: AssetRingtoneDataSource,
    ): RingtoneDataSource = assetRingtoneDataSource


    @Provides
    @IntoSet
    fun provideStorageRingtoneDataSource(
        storageRingtoneDataSource: StorageRingtoneDataSource,
    ): RingtoneDataSource = storageRingtoneDataSource

}