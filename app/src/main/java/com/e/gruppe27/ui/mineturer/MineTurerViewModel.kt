package com.e.gruppe27.ui.mineturer

import android.app.Application
import androidx.lifecycle.*
import com.e.gruppe27.model.Tur
import com.e.gruppe27.model.database.TurDatabase

class MineTurerViewModel(application: Application) : AndroidViewModel(application){

    private val repository : MineTurerRepository
    val alleTurer : LiveData<List<Tur>>

    init {
        val turDao = TurDatabase(application).turDao()
        repository = MineTurerRepository(turDao)
        alleTurer = repository.getTurer()

        }



}