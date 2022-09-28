package com.teguh.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseLogin(
    @SerializedName("error"        ) var error        : Boolean? = null,
    @SerializedName("message"     ) var message     : String? = null,
    @SerializedName("loginResult" ) var loginResult : LoginResult? = LoginResult()
) : Parcelable
