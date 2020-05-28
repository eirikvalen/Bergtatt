package com.e.gruppe27.model.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstanceWeather{
    private var retrofit : Retrofit? = null
    private var baseURL = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/1.9/"

    val retrofitInstance : Retrofit?
        get(){
            if(retrofit == null){
                retrofit = Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

}