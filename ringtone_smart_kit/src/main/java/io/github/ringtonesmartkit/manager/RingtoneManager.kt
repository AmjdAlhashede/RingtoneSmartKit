package io.github.ringtonesmartkit.manager

import io.github.ringtonesmartkit.api.RingtoneSmartKit
import io.github.ringtonesmartkit.domain.model.ContactIdentifier
import io.github.ringtonesmartkit.domain.model.RingtoneData
import io.github.ringtonesmartkit.domain.model.RingtoneSource
import io.github.ringtonesmartkit.domain.model.RingtoneTarget
import io.github.ringtonesmartkit.domain.model.RingtoneType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RingtoneManager @Inject constructor(
    private val ringtoneSmartKit: RingtoneSmartKit
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

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


    fun applyToTarget(
        source: RingtoneSource,
        target: RingtoneTarget,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        scope.launch {
            runCatching {
                ringtoneSmartKit.loadAndApply(source, target)
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
}
