package com.e.gruppe27.model.weather


data class Location(
    val fog: Fog,
    val maxTemperature: MaxTemperature,
    val minTemperature: MinTemperature,
    val symbol: Symbol,
    val temperature: Temperature,
    val windDirection: WindDirection,
    val windSpeed: WindSpeed
)