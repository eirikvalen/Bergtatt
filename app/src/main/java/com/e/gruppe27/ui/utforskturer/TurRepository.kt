package com.e.gruppe27.ui.utforskturer


import android.app.Application
import com.e.gruppe27.model.Tur
import com.google.gson.Gson


private const val FILNAVN_TURER = "turer.json"
class TurRepository(private val application: Application) {

    fun hentAlleTurer() : List<Tur> {
        val file = application.applicationContext.assets?.open(FILNAVN_TURER)?.bufferedReader().use {
            it?.readText()
        }

        val gson = Gson()
        return gson.fromJson(file, Array<Tur>::class.java).toList()


    }
}





