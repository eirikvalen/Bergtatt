package com.e.gruppe27.model.weather

import retrofit2.http.GET
import retrofit2.http.Query

interface LocationForecastService{

    @GET(".json")
    suspend fun getCurrentWeather(
        @Query("lat") latitude : Double,
        @Query("lon") longitude : Double
    ) : LocationForecast

}