package com.e.gruppe27.model.regionaltSkred


import com.google.gson.annotations.SerializedName

data class AvalancheRegionalItem(
    @SerializedName("AvalancheWarningList")
    val avalancheWarningList: List<AvalancheWarning>
)