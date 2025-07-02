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
import dagger.Provides
import dagger.multibindings.IntoSet
import io.github.ringtonesmartkit.data.strategy.mimetype.AssetUriMimeTypeExtractor
import io.github.ringtonesmartkit.data.strategy.mimetype.ContentUriMimeTypeExtractor
import io.github.ringtonesmartkit.data.strategy.mimetype.FileUriMimeTypeExtractor
import io.github.ringtonesmartkit.data.strategy.mimetype.handler.MimeTypeExtractorHandlerImpl
import io.github.ringtonesmartkit.domain.strategy.mimetype.MimeTypeExtractor
import io.github.ringtonesmartkit.domain.strategy.mimetype.MimeTypeExtractorHandler

@Module
internal abstract class MimeTypeExtractorAbstract {

    @Binds
    abstract fun provideMimeTypeExtractorHandler(
        mimeTypeExtractorHandlerImpl: MimeTypeExtractorHandlerImpl,
    ): MimeTypeExtractorHandler


    @Binds
    @IntoSet
    abstract fun bindAssetUriMimeTypeExtractor(
        impl: AssetUriMimeTypeExtractor
    ): MimeTypeExtractor

    @Binds
    @IntoSet
    abstract fun bindContentUriMimeTypeExtractor(
        impl: ContentUriMimeTypeExtractor
    ): MimeTypeExtractor

    @Binds
    @IntoSet
    abstract fun bindFileUriMimeTypeExtractor(
        impl: FileUriMimeTypeExtractor
    ): MimeTypeExtractor
}

