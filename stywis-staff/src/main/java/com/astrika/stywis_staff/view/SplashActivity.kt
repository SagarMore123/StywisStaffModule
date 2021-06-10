package com.astrika.stywis_staff.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.astrika.stywis_staff.R
import com.astrika.stywis_staff.master_controller.sync.MasterSyncIntentService
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.utils.CustomProgressBar
import com.astrika.stywis_staff.utils.RootUtil
import com.astrika.stywis_staff.view.dashboard.DashboardActivity
import com.astrika.stywis_staff.view.login.UserLoginActivity

class SplashActivity : AppCompatActivity() {

    private var isFirstTime: Boolean = true
    var progressBar = CustomProgressBar()

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startImmediateSync(this)
        setContentView(R.layout.activity_splash_staff)
        sharedPreferences = Constants.getSharedPreferences(this)

        isFirstTime = Constants.getIsFirstTime(this@SplashActivity)

        Handler().postDelayed(
            {
                // Tamper Checking and restrictions for installing the application on rooted devices
                if (RootUtil.isDeviceRooted) {
                    Constants.showToastMessage(this, "Your Device Is Rooted")
                } else {

                    if (isFirstTime) {

/*
                        NetworkUrls.LOGIN_MASTERS = "https://"

                        Constants.changeActivity<LoginUserActivity>(this@SplashActivity)

                        val bLive = MutableLiveData<Boolean>(false)

                        LoginUserFragment.changeBtnColor(resources.getDrawable(R.drawable.rounded_button_drawable))
                        bLive.observe(this, Observer {
                            Constants.changeActivity<DashboardActivity>(this@SplashActivity)
                        })
                        LoginViewModel.updateScreen(this,bLive)
*/

                        Constants.changeActivity<UserLoginActivity>(this@SplashActivity)
//                        val intent = Intent(this, LoginUserActivity::class.java)
//                        startActivityForResult(intent, 1)
//                        com.example.userlogin.network.NetworkController.SERVER_ERROR


                    } else {
                        Constants.changeActivity<DashboardActivity>(this@SplashActivity)
//                        redirectToDiscountScreen(Constants.getAccessToken(this) ?: "")
                    }
//                    finish()
                }
            }, Constants.SPLASH_TIME_OUT
        )

    }


    private fun startImmediateSync(context: Context) {
        val intentToSyncImmediately = Intent(context, MasterSyncIntentService::class.java)
        intentToSyncImmediately.putExtra(Constants.IS_SPLASH_MASTER, true)
        context.startService(intentToSyncImmediately)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressBar.dialog != null) {
            progressBar.dialog?.dismiss()
        }
    }

    private fun redirectToDiscountScreen(accessToken: String) {
/*

        val bundle = Bundle()
        val intent = Intent(this, DiscountManagementActivity::class.java)
        bundle.putString("accessToken", accessToken)
        bundle.putLong("outletId", 60)
        bundle.putLong("productId", 95)
        intent.putExtras(bundle)
        startActivity(intent)

        progressBar?.show(
            this,
            "Please Wait...\nIt will take few minutes"
        )
        progressBar.dialog?.setCancelable(false)

        com.astrika.checqk.discount.master_controller.sync.SyncData.loginMasterServicesCounts.observe(
            this,
            Observer {
                if (it == -1) {
                    progressBar.dialog?.dismiss()
                    SnackbarUtils.showSnackbar(root, NetworkController.SERVER_ERROR)

                } else if (it == com.astrika.checqk.discount.master_controller.sync.SyncData.loginMasterServices) {
*/
/*
                    DiscountViewModel.setAccessTokenOutletId(
                        com.astrika.stywis_staff.network.NetworkController.accessToken,
                        52,
                        95
                    )
*//*

                    progressBar.dialog?.dismiss()
                }

            })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 1) {

            if (data != null && data.hasExtra("accessToken") && !data?.getStringExtra("accessToken")
                    .isNullOrBlank()
            ) {
                sharedPreferences.edit()?.putString(
                    Constants.ACCESS_TOKEN,
                    Constants.encrypt(data.getStringExtra("accessToken") ?: "")
                )?.apply()
                redirectToDiscountScreen(data.getStringExtra("accessToken") ?: "")
            }
        }
*/
    }

}
