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
import io.github.ringtonesmartkit.data.model.RingtoneContactParams
import io.github.ringtonesmartkit.data.model.RingtoneSystemParams
import io.github.ringtonesmartkit.data.strategy.apply.contact.ContactByIdRingtoneApplyStrategy
import io.github.ringtonesmartkit.data.strategy.apply.contact.ContactByInteractiveRingtoneApplyStrategy
import io.github.ringtonesmartkit.data.strategy.apply.contact.ContactByPhoneRingtoneApplyStrategy
import io.github.ringtonesmartkit.data.strategy.apply.contact.ContactByUriRingtoneApplyStrategy
import io.github.ringtonesmartkit.data.strategy.apply.handler.RingtoneTargetApplyHandlerImpl
import io.github.ringtonesmartkit.data.strategy.apply.system.SystemAlarmRingtoneApplyStrategy
import io.github.ringtonesmartkit.data.strategy.apply.system.SystemCallRingtoneApplyStrategy
import io.github.ringtonesmartkit.data.strategy.apply.system.SystemNotificationRingtoneApplyStrategy
import io.github.ringtonesmartkit.domain.model.ContactInfo
import io.github.ringtonesmartkit.domain.strategy.applier.RingtoneApplyStrategy
import io.github.ringtonesmartkit.domain.strategy.applier.RingtoneParams
import io.github.ringtonesmartkit.domain.strategy.applier.RingtoneTargetApplyHandler


@Module
internal abstract class ApplyTargetModule {

    @Binds
    abstract fun bindSystemHandler(
        impl: RingtoneTargetApplyHandlerImpl<RingtoneSystemParams, Boolean>
    ): RingtoneTargetApplyHandler<RingtoneSystemParams, Boolean>

    @Binds
    abstract fun bindContactHandler(
        impl: RingtoneTargetApplyHandlerImpl<RingtoneContactParams, ContactInfo>
    ): RingtoneTargetApplyHandler<RingtoneContactParams, ContactInfo>

    @Binds
    @IntoSet
    abstract fun bindSystemCallRingtoneApplyStrategy(
        impl: SystemCallRingtoneApplyStrategy
    ): RingtoneApplyStrategy<RingtoneSystemParams, Boolean>

    @Binds
    @IntoSet
    abstract fun bindSystemNotificationRingtoneApplyStrategy(
        impl: SystemNotificationRingtoneApplyStrategy
    ): RingtoneApplyStrategy<RingtoneSystemParams, Boolean>

    @Binds
    @IntoSet
    abstract fun bindSystemAlarmRingtoneApplyStrategy(
        impl: SystemAlarmRingtoneApplyStrategy
    ): RingtoneApplyStrategy<RingtoneSystemParams, Boolean>

    @Binds
    @IntoSet
    abstract fun bindContactByIdRingtoneApplyStrategy(
        impl: ContactByIdRingtoneApplyStrategy
    ): RingtoneApplyStrategy<RingtoneContactParams, ContactInfo>

    @Binds
    @IntoSet
    abstract fun bindContactByUriRingtoneApplyStrategy(
        impl: ContactByUriRingtoneApplyStrategy
    ): RingtoneApplyStrategy<RingtoneContactParams, ContactInfo>

    @Binds
    @IntoSet
    abstract fun bindContactByPhoneRingtoneApplyStrategy(
        impl: ContactByPhoneRingtoneApplyStrategy
    ): RingtoneApplyStrategy<RingtoneContactParams, ContactInfo>

    @Binds
    @IntoSet
    abstract fun bindContactByInteractiveRingtoneApplyStrategy(
        impl: ContactByInteractiveRingtoneApplyStrategy
    ): RingtoneApplyStrategy<RingtoneContactParams, ContactInfo>
}
