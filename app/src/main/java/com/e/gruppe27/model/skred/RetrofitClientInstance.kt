package com.e.gruppe27.model.skred

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance{
    private var retrofit : Retrofit? = null
    private var baseURL = "https://api01.nve.no/hydrology/forecast/avalanche/v5.0.1/api/"

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