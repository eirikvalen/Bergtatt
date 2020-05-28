package com.e.gruppe27.ui.turinfo

import android.app.Application
import androidx.lifecycle.*
import com.e.gruppe27.model.Tur
import com.e.gruppe27.model.database.TurDatabase
import com.e.gruppe27.model.skred.AvalancheResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TurInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TurInfoRepository
    val alleTurer: LiveData<List<Tur>>

    init {
        val turDao = TurDatabase(application).turDao()
        repository = TurInfoRepository(turDao)
        alleTurer = repository.getTurer()
    }


    private val _avalancheData = MutableLiveData<AvalancheResponseItem>()

    val avalancheData: LiveData<AvalancheResponseItem>
        get() = _avalancheData

    //Henter skredvarsel på koordinat for en tur
    fun getAvalanche(lat: Double, long: Double, language: String, startdate: String, enddate: String) = viewModelScope.launch {
        val avalanche = repository.getAvalancheResponse(lat,long,language,startdate,enddate)
        _avalancheData.postValue(avalanche)

    }

    //legger tur til i database
    fun insert(tur: Tur) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(tur)
    }

    //fjerner valgt tur fra database
    fun delete(tur: Tur) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(tur)
    }






}