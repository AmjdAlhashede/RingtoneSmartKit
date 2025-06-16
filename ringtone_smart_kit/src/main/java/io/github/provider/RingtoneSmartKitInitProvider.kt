package io.github.provider

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import io.github.di.DaggerRingtoneSmartKitComponent
import io.github.di.RingtoneSmartKitComponent
import io.github.ringtonesmartkit.contract.ActivityTracker

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
