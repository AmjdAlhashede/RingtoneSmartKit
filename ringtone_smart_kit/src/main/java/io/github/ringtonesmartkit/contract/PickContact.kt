package io.github.ringtonesmartkit.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ComponentActivity
import kotlinx.coroutines.CompletableDeferred

internal class PickContacts : ActivityResultContract<Unit, Uri?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(Intent.ACTION_PICK).apply {
            type = "vnd.android.cursor.dir/contact"
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK) intent?.data else null
    }
}


internal suspend fun pickContact(): Uri? {
    val activity = ActivityTracker.currentActivity as? androidx.activity.ComponentActivity ?: return null

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


