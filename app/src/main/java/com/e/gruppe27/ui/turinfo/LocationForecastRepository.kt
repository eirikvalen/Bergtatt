package com.e.gruppe27.ui.turinfo

import com.e.gruppe27.model.weather.LocationForecastService
import com.e.gruppe27.model.weather.RetrofitClientInstanceWeather

class LocationForecastRepository{
    private val locationForecastService =
        RetrofitClientInstanceWeather.retrofitInstance?.create(LocationForecastService::class.java)


    suspend fun getCurrentWeather(lat : Double, long : Double)
        = locationForecastService?.getCurrentWeather(lat,long)



}