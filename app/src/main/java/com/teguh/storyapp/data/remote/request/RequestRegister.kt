package com.teguh.storyapp.data.remote.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestRegister(
    @SerializedName("name"         ) var name        : String? = null,
    @SerializedName("email"        ) var email        : String? = null,
    @SerializedName("password"     ) var password     : String? = null
): Parcelable {
    companion object {
        fun setRequestRegister(name: String, email: String, password: String): RequestRegister {
            return RequestRegister(name, email, password)
        }
    }
}
