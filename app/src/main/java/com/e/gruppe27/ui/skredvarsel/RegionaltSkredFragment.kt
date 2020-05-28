package com.e.gruppe27.ui.skredvarsel


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.e.gruppe27.R
import androidx.lifecycle.Observer
import com.e.gruppe27.model.regionaltSkred.AvalancheRegionalItem
import com.e.gruppe27.model.regionaltSkred.AvalancheWarning
import kotlinx.android.synthetic.main.fragment_regionaltskred.*
import kotlinx.android.synthetic.main.skred_region_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class RegionaltSkredFragment : Fragment() {

    private lateinit var hjemViewModel : RegionaltSkredViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root : View = inflater.inflate(R.layout.fragment_regionaltskred, container, false)

      if (isConnected()){
          try {
              hjemViewModel = ViewModelProvider(this).get(RegionaltSkredViewModel::class.java)

              //Oppdaterer View med objekter
              hjemViewModel.avalancheData.observe(viewLifecycleOwner, Observer {
                  skredRegionContainer.removeAllViews()
                  updateUI(it)
              })
          }catch (e: Exception){}
      }
        else{
           root = inflater.inflate(R.layout.no_internett_layout_fragment, container, false)
      }



        return root
    }

    private fun isConnected(): Boolean {
        var connected = false
        try {
            val cm =
                activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            println(e)
        }
        return connected
    }

    //Oppdaterer informasjonen på hovedskjerm
    private fun updateUI(avalanceRegionalList : List<AvalancheRegionalItem>){

        //Sorterer regionene basert på farenivå og filtrerer bort de som har farenivå 0.
        val sorted = avalanceRegionalList.sortedByDescending{ it.avalancheWarningList[0].dangerLevel.toInt()}
        val icons = arrayOf(R.id.warningIcon1, R.id.warningIcon2, R.id.warningIcon3)
        val sortedVurdert = sorted.filter {it.avalancheWarningList[0].dangerLevel.toInt() > 0}
        val (datoImorgen, datoIOverimorgen) = getDatoerFormatert()

        for(avalanche in sortedVurdert){
            val warningList = mutableListOf<AvalancheWarning>()
            for(i in avalanche.avalancheWarningList.indices) {
                val warning = avalanche.avalancheWarningList[i]
                warningList.add(warning)
            }
            val firstWarning = warningList[0]

            //Setter informasjon fra API til objekt
            with(LayoutInflater.from(activity).inflate(R.layout.skred_region_item, skredRegionContainer, false)){
                region_navn.text = firstWarning.regionName
                skredfare.text = getString(R.string.skredfare, firstWarning.dangerLevel)
                maintext.text = firstWarning.mainText
                imorgenTekst.text = datoImorgen
                iOverimorgen.text = datoIOverimorgen

                //Setter faretrekant basert på skredfarenivå fra API
                for ((i, avalancheWarning) in warningList.withIndex()){
                    val iconview = findViewById<ImageView>(icons[i])
                    when (avalancheWarning.dangerLevel) {
                        "1" -> iconview.setImageResource(R.drawable.ic_warning_1)
                        "2" -> iconview.setImageResource(R.drawable.ic_warning_2)
                        "3" -> iconview.setImageResource(R.drawable.ic_warning_3)
                        "4" -> iconview.setImageResource(R.drawable.ic_warning_4)
                        "5" -> iconview.setImageResource(R.drawable.ic_warning_5)
                        else -> iconview.setImageResource(R.drawable.ic_warning_null)
                    }
                }
                skredRegionContainer.addView(this)
            }
        }
    }

    //Henter dato formatert på ønsket måte
    private fun getDatoerFormatert(): Pair<String, String> {
        val cal = Calendar.getInstance()
        val utskriftDato = SimpleDateFormat("dd.MM", Locale.UK)

        cal.add(Calendar.DAY_OF_MONTH, 1)
        val datoImorgen = utskriftDato.format(cal.time)
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val datoOverimorgen = utskriftDato.format(cal.time)

        return Pair(datoImorgen, datoOverimorgen)
    }
}

