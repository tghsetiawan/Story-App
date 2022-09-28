package com.teguh.storyapp.data.remote.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestLogin(
    @SerializedName("email"        ) var email        : String? = null,
    @SerializedName("password"     ) var password     : String? = null
): Parcelable {
    companion object {
        fun setRequestLogin(email: String, password: String): RequestLogin {
            return RequestLogin(email, password)
        }
    }
}
