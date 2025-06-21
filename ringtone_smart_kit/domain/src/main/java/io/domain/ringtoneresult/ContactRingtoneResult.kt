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

package io.domain.ringtoneresult

import io.domain.applier.RingtoneApplyResult
import io.domain.applier.RingtoneApplyResultHandler
import io.domain.model.ContactInfo


sealed class ContactRingtoneResult : RingtoneApplyResult {
    data class Success(val info: ContactInfo) : ContactRingtoneResult()
    data class Failure(val throwable: Throwable) : ContactRingtoneResult()
}

class ContactRingtoneResultHandler : RingtoneApplyResultHandler {
    private var successCallback: ((ContactInfo) -> Unit)? = null
    private var failureCallback: ((Throwable) -> Unit)? = null
    private var doneCallback: (() -> Unit)? = null

    private var result: ContactRingtoneResult? = null

    fun onSuccess(block: (ContactInfo) -> Unit): ContactRingtoneResultHandler {
        successCallback = block
        result?.let { result->
            (result as? ContactRingtoneResult.Success)?.let { block(it.info) }
        }
        return this
    }

    fun onFailure(block: (Throwable) -> Unit): ContactRingtoneResultHandler {
        failureCallback = block
        result?.let {
            if (it is ContactRingtoneResult.Failure) block(it.throwable)
        }
        return this
    }

    fun onDone(block: () -> Unit): ContactRingtoneResultHandler {
        doneCallback = block
        return this
    }

    override fun invokeSuccess(result: RingtoneApplyResult) {
        if (result is ContactRingtoneResult.Success) {
            this.result = result
            successCallback?.invoke(result.info)
            doneCallback?.invoke()
        }
    }

    override fun invokeFailure(throwable: Throwable) {
        this.result = ContactRingtoneResult.Failure(throwable)
        failureCallback?.invoke(throwable)
        doneCallback?.invoke()

    }

    override fun invokeDone() {
        doneCallback?.invoke()
    }
}
