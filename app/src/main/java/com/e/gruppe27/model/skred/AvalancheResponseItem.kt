package com.e.gruppe27.model.skred
import com.google.gson.annotations.SerializedName


data class AvalancheResponseItem(
    @SerializedName("AvalancheAdvices")
    val avalancheAdvices: List<AvalancheAdvice>?,
    @SerializedName("AvalancheDanger")
    val avalancheDanger: String,
    @SerializedName("CurrentWeaklayers")
    val currentWeaklayers: String,
    @SerializedName("DangerLevel")
    val dangerLevel: String,
    @SerializedName("DangerLevelName")
    val dangerLevelName: String,
    @SerializedName("MainText")
    val mainText: String,
    @SerializedName("PublishTime")
    val publishTime: String,
    @SerializedName("RegionName")
    val regionName: String,
    @SerializedName("SnowSurface")
    val snowSurface: String
)