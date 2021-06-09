package com.astrika.stywis_staff.network.network_utils

import android.content.Context
import com.astrika.stywis_staff.network.NetworkController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

class NetworkUtils {
    companion object {
        const val HTTP_SUCCESS = 200
        const val HTTP_RETROFIT_FAILURE = 0
        const val TIMEOUT = 60 * 1000L


        private var viewModelJob = Job()

        val coroutineScope = CoroutineScope(
            viewModelJob + Dispatchers.Main
        )

        private fun updateClientDispatcherRequests(): Dispatcher {
            //ADD DISPATCHER WITH MAX REQUEST TO 1
            val dispatcher = Dispatcher()
            dispatcher.maxRequests = 1
            return dispatcher
        }

        private val client: OkHttpClient = OkHttpClient().newBuilder()
//val client: OkHttpClient = getOkHttpClient()
            .dispatcher(updateClientDispatcherRequests())
            .addInterceptor(AuthenticationInterceptorRefreshToken(this, NetworkController.mContext))
            .authenticator(RefreshTokenAuthenticator(NetworkController.mContext))
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS).build()

        var gSon: Gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()


        fun getOkHttpClient(context: Context?): OkHttpClient? {
            var client = OkHttpClient()


            // Certificate Pinning
            if (SERVER_URL.contains("https")) {
                val httpBuilder = OkHttpClient.Builder()
                val hostname = "uat-api.checqk.com"
                val certificatePinner = CertificatePinner.Builder()
                    .add(hostname, "sha256/MQKRJyWEHXlvA+oeNkeZooQ+gmWuBm48C211HSbNo5c=")
                    .build()
                val connectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                connectionSpec.tlsVersions(TlsVersion.TLS_1_2).build()
                val tlsSocketFactory: TLSSocketFactory
                try {
                    tlsSocketFactory = TLSSocketFactory(context)
                    httpBuilder.sslSocketFactory(
                        tlsSocketFactory,
                        tlsSocketFactory.systemDefaultTrustManager()
                    )
                    httpBuilder.hostnameVerifier { s, sslSession -> true }
                } catch (e: KeyManagementException) {

                } catch (e: NoSuchAlgorithmException) {
                }
                client = httpBuilder.certificatePinner(certificatePinner)
                    .connectionSpecs(listOf(connectionSpec.build()))
                    .build()
            } else {
                client = OkHttpClient()
            }
            return client
        }


        fun getStringResponseFromRaw(response: ResponseBody): String {
            val reader: BufferedReader?
            val sb = java.lang.StringBuilder()
            try {
                reader = BufferedReader(InputStreamReader(response.byteStream()))
                var line: String?
                try {
                    while (reader.readLine().also { line = it } != null) {
                        sb.append(line)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return sb.toString()
        }


        fun getStringResponseFromRaw(response: Response<ResponseBody>): String? {
            val reader: BufferedReader?
            val sb = java.lang.StringBuilder()
            try {
                reader = BufferedReader(InputStreamReader(response.body()!!.byteStream()))
                var line: String?
                try {
                    while (reader.readLine().also { line = it } != null) {
                        sb.append(line)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return sb.toString()
        }

    }
}

