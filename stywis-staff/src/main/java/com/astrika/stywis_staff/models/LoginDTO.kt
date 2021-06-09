package com.astrika.stywis_staff.models

import com.astrika.stywis_staff.utils.Constants

class LoginDTO {

    var deviceKey: String? = "123"
    var isForgotPassword: Boolean? = false
    var password: String? = ""
    var role: String? = ""
    var sourceId: String? = ""
    var username: String? = ""
    val productId = Constants.PRODUCT_ID_VALUE

}

class RefreshTokenDTO {
    var username: String? = ""
    var refreshToken: String? = ""
}

class AccessTokenDTO {
    var access_token: String? = ""
    var refresh_token: String? = ""
}