package com.github.ringtonesmartkit.viewmodules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ringtonesmartkit.api.RingtoneSmartKit
import com.github.ringtonesmartkit.domain.model.ContactIdentifier
import com.github.ringtonesmartkit.domain.model.RingtoneData
import com.github.ringtonesmartkit.domain.model.RingtoneSource
import com.github.ringtonesmartkit.domain.model.RingtoneTarget
import com.github.ringtonesmartkit.domain.model.RingtoneType
import com.github.ringtonesmartkit.extensions.runInCatchingBlock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ContactViewModel : ViewModel(), KoinComponent {

    private val kit by inject<RingtoneSmartKit>()

    fun loadRingtone(
        source: RingtoneSource,
        onLoaded: (RingtoneData?) -> Unit,
        onError: (Throwable) -> Unit = {},
    ) {

        launchInBackground {
            runInCatchingBlock {
                kit.load(source)
            }.onSuccess(onLoaded)
                .onFailure(onError)
        }
    }


    /**
     * تحميل وتطبيق نغمة على نوع نظام معين (CALL / NOTIFICATION / ALARM)
     */
    fun setSystemRingtone(
        source: RingtoneSource,
        type: RingtoneType = RingtoneType.CALL,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        applyToTarget(
            source = source,
            target = mapSystemTypeToTarget(type),
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun setContactRingtone(
        source: RingtoneSource,
        contact: ContactIdentifier,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        applyToTarget(
            source = source,
            target = RingtoneTarget.ContactTarget.Provided(contact),
            onSuccess = onSuccess,
            onError = onError
        )
    }


    /**
     * تحميل وتطبيق نغمة على هدف معين (بما فيه Custom Target)
     */
    fun applyToTarget(
        source: RingtoneSource,
        target: RingtoneTarget,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        launchInBackground {
            runInCatchingBlock {
                kit.loadAndApply(source, target)
            }.onSuccess {
                onSuccess()
            }.onFailure(onError)
        }
    }

    private fun mapSystemTypeToTarget(type: RingtoneType): RingtoneTarget.System = when (type) {
        RingtoneType.CALL -> RingtoneTarget.System.Call
        RingtoneType.NOTIFICATION -> RingtoneTarget.System.Notification
        RingtoneType.ALARM -> RingtoneTarget.System.Alarm
    }

    private fun launchInBackground(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            block()
        }
    }

}