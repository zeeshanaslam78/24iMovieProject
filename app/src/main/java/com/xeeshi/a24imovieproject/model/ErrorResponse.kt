package com.xeeshi.a24imovieproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ErrorResponse {

    @SerializedName("status_message")
    @Expose
    var statusMessage: String? = null
    @SerializedName("success")
    @Expose
    var success: Boolean? = null
    @SerializedName("status_code")
    @Expose
    var statusCode: Int? = null

}