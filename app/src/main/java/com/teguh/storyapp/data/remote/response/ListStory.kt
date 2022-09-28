package com.teguh.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListStory (
    @SerializedName("id"          ) var id          : String,
    @SerializedName("name"        ) var name        : String,
    @SerializedName("description" ) var description : String,
    @SerializedName("photoUrl"    ) var photoUrl    : String,
    @SerializedName("createdAt"   ) var createdAt   : String,
    @SerializedName("lat"         ) var lat         : Double,
    @SerializedName("lon"         ) var lon         : Double
) : Parcelable