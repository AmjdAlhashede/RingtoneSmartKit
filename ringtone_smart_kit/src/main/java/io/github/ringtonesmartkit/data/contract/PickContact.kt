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

package io.github.ringtonesmartkit.data.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.net.toUri
import io.github.ringtonesmartkit.domain.contact.ActivityTracker
import kotlinx.coroutines.CompletableDeferred

internal class PickContacts : ActivityResultContract<Unit, Uri?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK) intent?.data else null
    }
}

internal suspend fun pickContact(): Uri? {
    val activity = ActivityTracker.currentActivity as? ComponentActivity ?: return null

    val resultDeferred = CompletableDeferred<Uri?>()

    val launcher = activity.activityResultRegistry.register(
        "pick_contact_launcher",
        PickContacts()
    ) { uri ->
        resultDeferred.complete(uri)
    }

    launcher.launch(Unit)
    val result = resultDeferred.await()
    launcher.unregister()
    return result
}


