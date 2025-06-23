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

package io.github.ringtonesmartkit.provider

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import io.github.ringtonesmartkit.di.DaggerRingtoneSmartKitComponent
import io.github.ringtonesmartkit.di.RingtoneSmartKitComponent
import io.github.ringtonesmartkit.domain.contact.ActivityTracker

class RingtoneSmartKitInitProvider : ContentProvider() {

    internal companion object {
        lateinit var component: RingtoneSmartKitComponent
            private set
    }

    override fun onCreate(): Boolean {
        val appContext = context?.applicationContext ?: return false
        (appContext as? Application)?.registerActivityLifecycleCallbacks(ActivityTracker)

        component = DaggerRingtoneSmartKitComponent.builder()
            .context(appContext)
            .build()

        return true
    }

    override fun delete(
        p0: Uri,
        p1: String?,
        p2: Array<out String?>?,
    ): Int {
        return -1
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun query(
        p0: Uri,
        p1: Array<out String?>?,
        p2: String?,
        p3: Array<out String?>?,
        p4: String?,
    ): Cursor? {
        return null
    }

    override fun update(
        p0: Uri,
        p1: ContentValues?,
        p2: String?,
        p3: Array<out String?>?,
    ): Int {
        return -1
    }
}
