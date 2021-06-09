package com.astrika.stywis_staff.network

import com.astrika.stywis_staff.network.network_utils.NetworkUtils.Companion.retrofit
import com.astrika.stywis_staff.network.services.DashboardService
import com.astrika.stywis_staff.network.services.MastersService
import com.astrika.stywis_staff.network.services.UserService

object UserApi {

    val retrofitService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val mastersRetrofitService: MastersService by lazy {
        retrofit.create(MastersService::class.java)
    }

    val retrofitDashboardService: DashboardService by lazy {
        retrofit.create(DashboardService::class.java)
    }


}