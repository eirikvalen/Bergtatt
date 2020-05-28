package com.e.gruppe27.ui.skredvarsel

import com.e.gruppe27.model.skred.AvalancheResponseService
import com.e.gruppe27.model.skred.RetrofitClientInstance

class RegionaltSkredRepository {

     var avalancheRegionalService = RetrofitClientInstance.retrofitInstance?.create(
        AvalancheResponseService::class.java)

    //Henter Regional skredinformasjon
     suspend fun getAvalancheResponse(language: String, startdate: String, enddate: String)
            = avalancheRegionalService?.getAvalancheDataRegional(language,startdate,enddate)


}
