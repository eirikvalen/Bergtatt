package com.e.gruppe27.ui.mineturer

import androidx.lifecycle.LiveData
import com.e.gruppe27.model.Tur
import com.e.gruppe27.ui.turinfo.TurDao

class MineTurerRepository(private val turDao: TurDao){

    //Returnerer liste med turer fra Database
    fun getTurer() : LiveData<List<Tur>> {
        return turDao.getTurer()
    }
    

}