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

import dagger.Binds
import dagger.Module
import io.amjdalhashede.data.applier.RingtoneAssetsApplierImpl
import io.amjdalhashede.data.applier.RingtoneStorageApplierImpl
import io.amjdalhashede.data.repository.RingtoneManagerImpl
import io.amjdalhashede.data.repository.RingtoneRepositoryImpl
import io.domain.applier.RingtoneAssetsApplier
import io.domain.applier.RingtoneStorageApplier
import io.domain.repository.RingtoneManager
import io.domain.repository.RingtoneRepository

@Module
abstract class RingtoneModule {

    @Binds
    abstract fun provideRingtoneRepository(
        ringtoneRepositoryImpl: RingtoneRepositoryImpl,
    ): RingtoneRepository

    @Binds
    abstract fun provideRingtoneManager(
        ringtoneManagerImpl: RingtoneManagerImpl,
    ): RingtoneManager

    @Binds
    abstract fun provideRingtoneAssetsApplier(
        ringtoneAssetsApplierImpl: RingtoneAssetsApplierImpl,
    ): RingtoneAssetsApplier

    @Binds
    abstract fun provideRingtoneStorageApplier(
        ringtoneStorageApplierImpl: RingtoneStorageApplierImpl,
    ): RingtoneStorageApplier


}