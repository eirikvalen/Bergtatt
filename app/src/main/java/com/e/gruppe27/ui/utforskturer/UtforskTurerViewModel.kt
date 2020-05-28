package com.e.gruppe27.ui.utforskturer

import android.app.Application
import androidx.lifecycle.AndroidViewModel



class UtforskTurerViewModel(application: Application) : AndroidViewModel(application) {

    private val turerRepository = TurRepository(application)

    fun hentAlleTurer() = turerRepository.hentAlleTurer()

}
