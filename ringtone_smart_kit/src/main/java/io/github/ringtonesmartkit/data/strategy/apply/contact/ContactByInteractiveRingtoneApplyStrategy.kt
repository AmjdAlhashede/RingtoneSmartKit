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

package io.github.ringtonesmartkit.data.strategy.apply.contact

import android.content.Context
import androidx.core.net.toUri
import io.github.ringtonesmartkit.data.model.RingtoneContactParams
import io.github.ringtonesmartkit.domain.model.ContactInfo
import io.github.ringtonesmartkit.domain.strategy.applier.RingtoneApplyStrategy
import io.github.ringtonesmartkit.domain.model.ContactTarget
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.presentation.lifecycle.pickContact
import io.github.ringtonesmartkit.utils.extensions.applyToContact
import io.github.ringtonesmartkit.utils.extensions.getContactInfoFromUri

import javax.inject.Inject

internal class ContactByInteractiveRingtoneApplyStrategy @Inject constructor(
    private val context: Context,
) : RingtoneApplyStrategy<RingtoneContactParams, ContactInfo?> {


    override fun canHandler(target: RingtoneTarget): Boolean {
        return target is ContactTarget.Interactive
    }

    override suspend fun apply(param: RingtoneContactParams): ContactInfo? {
        param.target as? ContactTarget.Interactive
            ?: throw IllegalArgumentException("this Contact Identifier Not Supported In ByInteractiveRingtoneApplyStrategy Strategy")


        val uri = pickContact()

        val contactInfo = uri?.let {
            getContactInfoFromUri(
                context = context,
                contactUri = it
            )
        }

        contactInfo?.let {
            applyToContact(
                context = context,
                insertedUri = param.insertedUri.toUri(),
                contactInfo = it
            )
        }


        return contactInfo
    }
}