package com.e.gruppe27.model.weather


data class Time(
    val datatype: String,
    val from: String,
    val location: Location,
    val to: String
)