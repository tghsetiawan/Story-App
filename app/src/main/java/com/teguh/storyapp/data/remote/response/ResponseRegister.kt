package com.teguh.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseRegister(
    @SerializedName("error"        ) var error        : Boolean? = null,
    @SerializedName("message"     ) var message     : String? = null
) : Parcelable
