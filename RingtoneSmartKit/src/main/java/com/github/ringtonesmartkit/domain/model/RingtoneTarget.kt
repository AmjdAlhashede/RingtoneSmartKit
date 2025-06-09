package com.github.ringtonesmartkit.domain.model

sealed class RingtoneTarget {

    sealed class System : RingtoneTarget() {
        object Call : System()
        object Notification : System()
        object Alarm : System()
    }

    sealed class ContactTarget : RingtoneTarget() {
        data class Provided(val identifier: ContactIdentifier) : ContactTarget()
    }

}
