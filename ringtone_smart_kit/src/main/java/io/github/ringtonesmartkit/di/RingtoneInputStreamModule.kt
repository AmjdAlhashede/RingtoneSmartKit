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

package io.github.ringtonesmartkit.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import io.github.ringtonesmartkit.data.strategy.inputstreamprovider.AssetUriInputStreamProvider
import io.github.ringtonesmartkit.data.strategy.inputstreamprovider.ContentUriInputStreamProvider
import io.github.ringtonesmartkit.data.strategy.inputstreamprovider.FileUriInputStreamProvider
import io.github.ringtonesmartkit.data.strategy.inputstreamprovider.handler.RingtoneInputStreamProviderHandlerImpl
import io.github.ringtonesmartkit.domain.strategy.inputstreamprovider.RingtoneInputStreamProvider
import io.github.ringtonesmartkit.domain.strategy.inputstreamprovider.RingtoneInputStreamProviderHandler

@Module
internal abstract class RingtoneInputStreamModule {

    @Binds
    abstract fun provideRingtoneInputStreamHandler(
        ringtoneInputStreamHandlerImpl: RingtoneInputStreamProviderHandlerImpl,
    ): RingtoneInputStreamProviderHandler


    @Binds
    @IntoSet
    abstract fun bindAssetUriInputStreamProvider(
        impl: AssetUriInputStreamProvider,
    ): RingtoneInputStreamProvider

    @Binds
    @IntoSet
    abstract fun bindContentUriInputStreamProvider(
        impl: ContentUriInputStreamProvider,
    ): RingtoneInputStreamProvider

    @Binds
    @IntoSet
    abstract fun bindFileUriInputStreamProvider(
        impl: FileUriInputStreamProvider,
    ): RingtoneInputStreamProvider

}
