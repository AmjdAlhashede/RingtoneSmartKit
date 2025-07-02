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

package io.github.ringtonesmartkit.domain.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun interface SuccessCallback<R> {
    fun onSuccess(result: R)
}

fun interface FailureCallback {
    fun onFailure(e: Throwable)
}

fun interface DoneCallback {
    fun onDone()
}

class RingtoneResultHandler<R> internal constructor(
    private val job: suspend () -> R,
) {
    private var onSuccessCallback: SuccessCallback<R>? = null
    private var onFailureCallback: FailureCallback? = null
    private var onDoneCallback: DoneCallback? = null


    fun onSuccess(callback: SuccessCallback<R>): RingtoneResultHandler<R> {
        onSuccessCallback = callback
        return this
    }

    fun onFailure(callback: FailureCallback): RingtoneResultHandler<R> {
        onFailureCallback = callback
        return this
    }

    fun onDone(callback: DoneCallback): RingtoneResultHandler<R> {
        onDoneCallback = callback
        return this
    }

    fun launch(scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)) {
        scope.launch {
            try {
                val result = job()
                withContext(Dispatchers.Main) {
                    onSuccessCallback?.onSuccess(result)
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) {
                    onFailureCallback?.onFailure(e)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    onDoneCallback?.onDone()
                }
            }
        }
    }
}
