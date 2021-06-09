package com.astrika.stywis_staff.models

import android.net.Uri
import java.io.Serializable

class ImageDTO : Serializable {
    var id: Long? = 0
    var documentName: String? = ""
    var base64: String? = ""
    var path: String? = ""
    var imageUri: Uri? = null
    var isUri: Boolean? = false
}