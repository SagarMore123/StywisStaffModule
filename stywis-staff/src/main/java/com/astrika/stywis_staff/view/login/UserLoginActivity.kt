package com.astrika.stywis_staff.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.master_controller.sync.SyncData
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.SnackbarUtils.showSnackbar
import com.astrika.stywis_staff.view.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_login_staff.*

class UserLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_staff)


        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey(Constants.ACCESS_TOKEN_INVALID)) {
                if (bundle.getBoolean(Constants.ACCESS_TOKEN_INVALID)) {
                    showSnackbar(
                        root,
                        "User has been logged out"
                    )
                }
            }
        }

        SyncData.loginMasterServicesCounts.value = 0

        SyncData.loginMasterServicesCounts.observe(this, Observer {

            if (it == SyncData.loginMasterServices) {
                val intent = Intent(this, DashboardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
        })

    }


    override fun onBackPressed() {

    }


}
