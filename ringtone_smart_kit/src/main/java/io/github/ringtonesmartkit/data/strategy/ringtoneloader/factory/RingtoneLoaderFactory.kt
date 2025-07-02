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

package io.github.ringtonesmartkit.data.strategy.ringtoneloader.factory

import io.github.ringtonesmartkit.data.strategy.ringtoneloader.LegacyRingtoneLoader
import io.github.ringtonesmartkit.data.strategy.ringtoneloader.ModernRingtoneLoader
import io.github.ringtonesmartkit.domain.strategy.ringtoneloader.RingtoneLoader
import io.github.ringtonesmartkit.utils.extensions.sdkQAndUp
import javax.inject.Inject

internal class RingtoneLoaderFactory @Inject constructor(
    private val legacyRingtoneLoader: LegacyRingtoneLoader,
    private val modernRingtoneLoader: ModernRingtoneLoader,
) {

    fun getLoader(): RingtoneLoader {
        return sdkQAndUp {
            modernRingtoneLoader
        } ?: legacyRingtoneLoader
    }
}