package com.e.gruppe27.model.regionaltSkred


import com.google.gson.annotations.SerializedName

data class AvalancheWarning(
    @SerializedName("DangerLevel")
    val dangerLevel: String,
    @SerializedName("MainText")
    val mainText: String,
    @SerializedName("RegionName")
    val regionName: String

)