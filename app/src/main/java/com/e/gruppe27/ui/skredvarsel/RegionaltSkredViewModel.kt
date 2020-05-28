package com.e.gruppe27.ui.skredvarsel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.gruppe27.model.regionaltSkred.AvalancheRegionalItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


private const val NORSK_LANDS_KODE = "1"
private const val DATO_FORMAT = "yyyy-MM-dd"
private const val TO_DAGER_FREM = 2

class RegionaltSkredViewModel : ViewModel(){

    private val repository = RegionaltSkredRepository()
    private val _avalancheData = MutableLiveData<List<AvalancheRegionalItem>>()

   //ViewModelen har MutableLiveData som holder på data men har en getter metode som returnerer LiveData for
   // hindre at man kan modifisere verdien fra utsiden av klassen.

    val avalancheData : LiveData<List<AvalancheRegionalItem>>
        get() = _avalancheData

    init{
        val(idag, sluttdato) = getDatoer()
        getAvalanche(idag, sluttdato)
    }


    //Henter Regional skredoversikt med 3 dagers skredvurdering
     private fun getAvalanche(startdate: String, enddate: String) = viewModelScope.launch {
        val avalanche = repository.getAvalancheResponse(NORSK_LANDS_KODE,startdate,enddate)
        _avalancheData.postValue(avalanche)

    }

    //Henter datoen idag og to dager frem til API kall
    private fun getDatoer() : Pair<String,String>{
        val cal = Calendar.getInstance()
        val hentDato: Date = cal.time
        val onsketFormat = SimpleDateFormat(DATO_FORMAT, Locale.UK)
        val idag : String = onsketFormat.format(hentDato)

        cal.add(Calendar.DAY_OF_MONTH, TO_DAGER_FREM)
        val sluttdato = onsketFormat.format(cal.time)

        return Pair(idag,sluttdato)
    }
}
