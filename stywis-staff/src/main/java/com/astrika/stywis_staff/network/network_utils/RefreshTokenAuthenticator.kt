package com.astrika.stywis_staff.network.network_utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.astrika.stywis_staff.models.RefreshTokenDTO
import com.astrika.stywis_staff.network.NetworkController
import com.astrika.stywis_staff.network.UserApi
import com.astrika.stywis_staff.utils.Constants
import com.astrika.stywis_staff.view.login.UserLoginActivity
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class RefreshTokenAuthenticator(private val context: Context) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        //MAKE SYNCHRONIZED
        synchronized(this) {
            if (!response.request().header("Authorization")
                    .equals(Constants.getAccessToken(context))
            )
                return null


            val sharedPreferences =
                context.let { Constants.getSharedPreferences(it) }

            val refreshTokenDTO = RefreshTokenDTO()
            refreshTokenDTO.username =
                Constants.decrypt(
                    sharedPreferences.getString(
                        Constants.EMAIL_ID,
                        ""
                    )
                )
            refreshTokenDTO.refreshToken = Constants.getRefreshToken(context)

            val responseNewTokenLoginModel =
                UserApi.retrofitService.refreshToken(refreshTokenDTO).execute()

            return when {
                responseNewTokenLoginModel.code() != 200 -> {
//                AuthManager().authExpiredAndGoLogin(AndroidApplication().getContext())
                    val intent = Intent(context, UserLoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra(Constants.ACCESS_TOKEN_INVALID, true)
                    if (context != null) {
                        Constants.clearSharedPrefs(context)
                    }
                    context.startActivity(intent)
                    null
                }
                else -> {

                    responseNewTokenLoginModel.body()?.access_token?.let {

                        Log.e("Success Refresh", it)

                        NetworkController.accessToken = "Bearer $it"
                        sharedPreferences.edit()?.putString(
                            Constants.ACCESS_TOKEN,
                            Constants.encrypt(it)
                        )?.apply()


                    }
                    /* if (response.request().headers().toString().contains("Authorization")){
                    response.request().newBuilder().header() = response.request().newBuilder().removeHeader("Authorization")
                }*/
                    return response.request().newBuilder().header(
                        "Authorization",
                        "Bearer " + responseNewTokenLoginModel.body()?.access_token
                    ).build()
//                chain.proceed(newAuthenticationRequest)
                }
            }

        }
    }
}