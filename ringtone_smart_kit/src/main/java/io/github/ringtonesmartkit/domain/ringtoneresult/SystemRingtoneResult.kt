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

package io.github.ringtonesmartkit.domain.ringtoneresult

import io.github.ringtonesmartkit.domain.applier.RingtoneApplyResult
import io.github.ringtonesmartkit.domain.applier.RingtoneApplyResultHandler

sealed class SystemRingtoneResult : RingtoneApplyResult {
    object Success : SystemRingtoneResult()
    data class Failure(val throwable: Throwable) : SystemRingtoneResult()
}

class SystemRingtoneResultHandler : RingtoneApplyResultHandler {
    private var successCallback: (() -> Unit)? = null
    private var failureCallback: ((Throwable) -> Unit)? = null
    private var doneCallback: (() -> Unit)? = null

    private var isCompleted = false
    private var isSuccess: Boolean? = null
    private var failureThrowable: Throwable? = null

    fun onSuccess(block: () -> Unit): SystemRingtoneResultHandler {
        successCallback = block
        if (isSuccess == true) block()
        return this
    }

    fun onFailure(block: (Throwable) -> Unit): SystemRingtoneResultHandler {
        failureCallback = block
        failureThrowable?.let { block(it) }
        return this
    }

    fun onDone(block: () -> Unit): SystemRingtoneResultHandler {
        doneCallback = block
        if (isCompleted) block()
        return this
    }

    override fun invokeSuccess(result: RingtoneApplyResult) {
        if (result is SystemRingtoneResult.Success) {
            isSuccess = true
            isCompleted = true
            successCallback?.invoke()
            doneCallback?.invoke()
        }
    }

    override fun invokeFailure(throwable: Throwable) {
        isSuccess = false
        isCompleted = true
        failureThrowable = throwable
        failureCallback?.invoke(throwable)
        doneCallback?.invoke()
    }

    override fun invokeDone() {
        doneCallback?.invoke()
    }
}
