package com.astrika.stywis_staff.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * A SingleLiveEvent used for Snackbar messages. Like a [SingleLiveEvent] but also prevents
 * null messages and uses a custom observer.
 *
 *
 * Note that only one observer is going to be notified of changes.
 */
class SnackbarMessage : SingleLiveEvent<String?>() {
    fun observe(owner: LifecycleOwner?, observer: SnackBarObserver) {
        super.observe(owner!!, Observer { s ->
            if (s == null) {
                return@Observer
            }
            observer.onNewMessage(s)
        })
    }

    interface SnackBarObserver {
        /**
         * Called when there is a new message to be shown.
         *
         * @param message The new message, non-null.
         */
        fun onNewMessage(message: String?)
    }
}