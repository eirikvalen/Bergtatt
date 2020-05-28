package com.e.gruppe27.model.skred

import com.e.gruppe27.model.regionaltSkred.AvalancheRegionalItem
import retrofit2.http.GET
import retrofit2.http.Path

interface AvalancheResponseService {

    //Henter detaljert skredinformasjon på lokasjon
    @GET("AvalancheWarningByCoordinates/Detail/{x}/{y}/{langkey}/{startdate}/{enddate}")
    suspend fun getAvalancheData(
        @Path("x") latitude : Double,
        @Path("y") longitude : Double,
        @Path("langkey") languageKey : String,
        @Path("startdate") startdate : String,
        @Path("enddate") enddate : String
    ) : List<AvalancheResponseItem>


   //Henter regional skredoversikt
    @GET("RegionSummary/Simple/{langkey}/{startdate}/{enddate}")
    suspend fun getAvalancheDataRegional(
        @Path("langkey") languageKey : String,
        @Path("startdate") startdate : String,
        @Path("enddate") enddate : String
    ) : List<AvalancheRegionalItem>

}

