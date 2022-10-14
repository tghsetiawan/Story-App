package com.teguh.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.teguh.storyapp.data.local.entity.StoryEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
class ResponseGetStory (
    @SerializedName("error"     ) var error     : Boolean?             = null,
    @SerializedName("message"   ) var message   : String?              = null,
    @SerializedName("listStory" ) var listStory : List<StoryEntity> = arrayListOf()
) : Parcelable
