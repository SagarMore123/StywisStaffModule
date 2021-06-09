package com.astrika.stywis_staff.master_controller.sync

import android.app.IntentService
import android.content.Intent
import com.astrika.stywis_staff.utils.Constants

class MasterSyncIntentService : IntentService("MasterSyncIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent?.getBooleanExtra(Constants.IS_SPLASH_MASTER, false) == true) {
            SyncData.splashSyncData(this)
        } else {
            SyncData.syncData(this)
        }
    }
}