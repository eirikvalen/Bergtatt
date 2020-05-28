package com.e.gruppe27.ui.turinfo

import androidx.lifecycle.LiveData
import com.e.gruppe27.model.Tur
import com.e.gruppe27.model.skred.AvalancheResponseService
import com.e.gruppe27.model.skred.RetrofitClientInstance

class TurInfoRepository(private val turDao: TurDao){

    private val avalancheResponseService =
        RetrofitClientInstance.retrofitInstance?.create(AvalancheResponseService::class.java)

    suspend fun getAvalancheResponse(lat: Double, long: Double, language: String, startdate: String, enddate: String)
           = avalancheResponseService?.getAvalancheData(lat, long, language, startdate, enddate)?.get(0)


    //legger tur inn i database
   fun insert(tur : Tur){
        turDao.insert(tur)
    }

    //fjerner tur fra database
   fun delete(tur: Tur){
        turDao.delete(tur)
    }

    //henter alle turer fra database
    fun getTurer() : LiveData<List<Tur>> {
        return turDao.getTurer()
    }


}


