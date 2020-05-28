package com.e.gruppe27.ui.turinfo



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.e.gruppe27.R
import com.e.gruppe27.model.Tur

class TurInfoFragment (val tur : Tur): Fragment(){

    private lateinit var turInfoViewModel: TurInfoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_turinfo, container, false)
        turInfoViewModel = ViewModelProvider(this).get(TurInfoViewModel::class.java)

        updateUI(root)

        return root
    }

    private fun updateUI(root : View){
        val turTittel: TextView = root.findViewById(R.id.tur_tittel)
        val turBilde: ImageView = root.findViewById(R.id.tur_bilde)
        val omraadeInnhold: TextView = root.findViewById(R.id.omrade_innhold)
        val varighetAntall: TextView = root.findViewById(R.id.tur_varighet)
        val distanseAntall: TextView = root.findViewById(R.id.tur_distanse)
        val typeInnhold: TextView = root.findViewById(R.id.tur_type)

        val graderingInnhold: TextView = root.findViewById(R.id.tur_gradering)
        val beskrivelse: TextView = root.findViewById(R.id.beskrivelse)


        turTittel.text = tur.navn
        Glide.with(this).load(tur.bildeSrc).into(turBilde)
        omraadeInnhold.text = tur.omrade
        varighetAntall.text = tur.varighet
        distanseAntall.text = getString(R.string.tur_distanse, tur.lengde)
        typeInnhold.text = tur.type

        graderingInnhold.text = tur.gradering
        beskrivelse.text = tur.beskrivelse

    }


}