package io.github.ringtonesmartkit.viewmodules

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ringtonesmartkit.contract.ActivityTracker
import io.github.ringtonesmartkit.contract.PickContacts
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class ContactPickerViewModel : ViewModel() {

    private var launcher: ActivityResultLauncher<Unit>? = null
    private var result: CompletableDeferred<Uri?>? = null
    private var key: String? = null

    fun registerIfNeeded(activity: ComponentActivity) {
        if (launcher != null) return

        key = "pick_contact_${System.currentTimeMillis()}"


        launcher = activity.activityResultRegistry.register(
            key!!,
            PickContacts()
        ) { uri ->
            result?.takeIf { it.isActive }?.complete(uri)
        }

        viewModelScope.launch(Dispatchers.Main) {
            activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    launcher?.unregister()
                    launcher = null
                    result?.cancel()
                }
            })
        }
    }

    suspend fun pickContact(): Uri? {
        val activity = ActivityTracker.currentActivity as? ComponentActivity ?: return null
        registerIfNeeded(activity)
        result = CompletableDeferred()
        launcher?.launch(Unit)
        return result?.await()
    }
}