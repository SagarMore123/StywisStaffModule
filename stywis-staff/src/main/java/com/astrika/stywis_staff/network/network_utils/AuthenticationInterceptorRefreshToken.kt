package com.astrika.stywis_staff.network.network_utils

import android.content.Context
import com.astrika.stywis_staff.network.NetworkController
import com.astrika.stywis_staff.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response


class AuthenticationInterceptorRefreshToken(
    private val networkUtils: NetworkUtils.Companion,
    private val context: Context?
) :
    Interceptor {


    private var networkController: NetworkController? = NetworkController.getInstance(context!!)
    private var sharedPreferences = Constants.getSharedPreferences(context!!)


    companion object {
        var isRefreshed = false
    }

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response? {

        var mainResponse: Response? = null
        //MAKE SYNCHRONIZED
//        synchronized(this) {
        val originalRequest = chain.request()

        var accessToken = Constants.getAccessToken(context = context) ?: ""
        if (originalRequest.url().toString().contains(LOGIN_MASTERS)
            || originalRequest.url().toString().contains(IS_FIRST_TIME_LOGIN_WITH_LOGIN_ID)
            || originalRequest.url().toString().contains(LOGIN_WITH_LOGIN_ID)
            || originalRequest.url().toString().contains(REFRESH_TOKEN)
        ) {
            accessToken = ""
        }
        val authenticationRequest = originalRequest.newBuilder()
            .addHeader("Authorization", accessToken).build()
//                .addHeader("appVersion", BuildConfig.VERSION_NAME).build()
        return chain.proceed(authenticationRequest)


/*
        mainResponse = initialResponse


        when {
            initialResponse.code() == 403 || initialResponse.code() == 401 -> {
                //RUN BLOCKING!!
//                val responseNewTokenLoginModel = runBlocking {


//                    val sharedPreferences = context?.let { Constants.getSharedPreferences(it) }

                val refreshTokenDTO = RefreshTokenDTO()
                refreshTokenDTO.username =
                    Constants.decrypt(
                        sharedPreferences?.getString(
                            Constants.EMAIL_ID,
                            ""
                        )
                    )
                refreshTokenDTO.refreshToken = Constants.getRefreshToken(context)

                var newAccessToken = ""

                try {

//                         UserApi.retrofitService.refreshToken(refreshTokenDTO).execute()

                    UserApi.retrofitService.refreshToken(refreshTokenDTO)
                        .enqueue(object : retrofit2.Callback<AccessTokenDTO> {
                            override fun onResponse(
                                call: Call<AccessTokenDTO>,
                                response: retrofit2.Response<AccessTokenDTO>
                            ) {
                                try {

                                    if (response.isSuccessful) {

                                        Log.e(
                                            "Success Refresh",
                                            response.body()?.access_token.toString()
                                        )

                                        NetworkController.accessToken =
                                            "Bearer ${response.body()?.access_token.toString()}"
                                        sharedPreferences.edit()?.putString(
                                            Constants.ACCESS_TOKEN,
                                            Constants.encrypt(response.body()?.access_token.toString())
                                        )?.apply()

                                        newAccessToken = NetworkController.accessToken
                                        accessToken = NetworkController.accessToken

                                        isRefreshed = true

                                        val newAuthenticationRequest =
                                            originalRequest.newBuilder().addHeader(
                                                "Authorization",
                                                Constants.getAccessToken(context = context) ?: ""
                                            ).build()
                                        mainResponse?.close()
                                        mainResponse = chain.proceed(newAuthenticationRequest)

                                    } else if (response.code() == 500) {

                                        val intent = Intent(context, UserLoginActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        intent.putExtra(Constants.ACCESS_TOKEN_INVALID, true)
                                        if (context != null) {
                                            Constants.clearSharedPrefs(context)
                                        }
                                        context?.startActivity(intent)
                                    }

//                                    response.raw().close()

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                } finally {

                                }

                            }

                            override fun onFailure(call: Call<AccessTokenDTO>, t: Throwable) {

                                try {
                                    if (t.message != null) {

                                        Log.e("Error Refresh", t.message.toString())
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                        })

                } catch (e: Exception) {
                    e.printStackTrace()
                }


                return mainResponse // Inner returns
//                    responseBody
//                }
*/
/*


                return when {
                    responseNewTokenLoginModel == null || responseNewTokenLoginModel.code() != 200 -> {

                        val intent = Intent(context, UserLoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra(Constants.ACCESS_TOKEN_INVALID, true)
                        if (context != null) {
                            Constants.clearSharedPrefs(context)
                        }
                        context?.startActivity(intent)
//                            null
//                            response
                        initialResponse.close()
                        val newAuthenticationRequest =
                            originalRequest.newBuilder().addHeader(
                                "Authorization",
                                ""
                            ).build()
                        chain.proceed(newAuthenticationRequest)

                    }
                    else -> {
                        responseNewTokenLoginModel.body()?.access_token?.let {
//                                loginDTO.updateToken(it)
                            NetworkController.accessToken = "Bearer $it"
                            sharedPreferences.edit()?.putString(
                                Constants.ACCESS_TOKEN,
                                Constants.encrypt(it)
                            )?.apply()

                            Log.e("accessToken", it)
//                                Log.e("refreshToken", responseNewTokenLoginModel.body().refresh_token)
                        }

                        responseNewTokenLoginModel.body()?.refresh_token?.let {

                            sharedPreferences?.edit()?.putString(
                                Constants.REFRESH_TOKEN,
                                Constants.encrypt(it)
                            )?.apply()

                            Log.e("refreshToken", it)
                        }


//                                Toast.makeText(context, "Hi , Refresh Token", Toast.LENGTH_LONG).show()
                        val newAuthenticationRequest =
                            originalRequest.newBuilder().addHeader(
                                "Authorization",
                                Constants.getAccessToken(context = context) ?: ""
                            ).build()
                        chain.proceed(newAuthenticationRequest)
                    }
                }
*//*



            }

            else -> { // Main Returns
                return mainResponse
*/
/*

                if (isRefreshed) {
                    isRefreshed = false
                    val newAuthenticationRequest =
                        originalRequest.newBuilder().addHeader(
                            "Authorization",
                            Constants.getAccessToken(context = context) ?: ""
                        ).build()
                    mainResponse?.close()
                    mainResponse = chain.proceed(newAuthenticationRequest)
                } else {
                    return mainResponse
                }

                return mainResponse
*//*

            }
        }

//        }

*/


    }


}