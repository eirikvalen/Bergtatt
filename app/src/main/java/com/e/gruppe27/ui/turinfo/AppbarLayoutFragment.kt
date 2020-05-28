package com.e.gruppe27.ui.turinfo

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.e.gruppe27.R
import com.e.gruppe27.model.Tur
import com.e.gruppe27.ui.MyPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AppbarLayoutFragment (var tur : Tur) : Fragment() {


    private lateinit var turInfoViewModel: TurInfoViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        turInfoViewModel = ViewModelProvider(this).get(TurInfoViewModel::class.java)
        var root = inflater.inflate(R.layout.turinfo_tabs, container, false)

        if (isConnected()){

                val viewpager: ViewPager2  = root.findViewById(R.id.view_pager)
                val knapp : ImageButton = root.findViewById(R.id.knapp)
                val lagreTurKnapp : ImageButton = root.findViewById(R.id.lagreTurKnapp)
                val fragmentAdapter = MyPagerAdapter(this, tur)
                val tabLayout : TabLayout = root.findViewById(R.id.tabLayout)
                val tabTitler: Array<String> = resources.getStringArray(R.array.tab_titler)

                //Bestemmer hva Tabs skal gjøre
                tabsSetUp(viewpager,fragmentAdapter)

                //Setter Tittel på tabs
                TabLayoutMediator(tabLayout, viewpager) { tab, position ->
                    tab.text = tabTitler[position]
                }.attach()

                knapp.setOnClickListener{
                    activity?.supportFragmentManager?.popBackStack()
                }

                //Sjekker at lagrede turer har gyldig farge == GUL opp mot favoritter database
                turInfoViewModel.alleTurer.observe(viewLifecycleOwner, Observer {
                    endreFargeStjerne(it, lagreTurKnapp)
                })

                //Legger Tur til favoritter of fjerner. Hva som skjer er basert på hvilke fragment man
                //befinner seg i.
                lagreTurKnapp.setOnClickListener{
                    oppdaterDatabase(lagreTurKnapp)
                }

        }else{

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

    //Setter opp tabs
    private fun tabsSetUp(viewpager: ViewPager2, fragmentAdapter : MyPagerAdapter){
        viewpager.isUserInputEnabled = false //hindrer sveip mellom tabs
        viewpager.offscreenPageLimit = 3 //bestemmer antall pages som er lastet inn i bakgrunnen
        viewpager.adapter = fragmentAdapter
    }

    //Bestemmer hva som skjer ved trykk av stjerneicon
    private fun oppdaterDatabase(lagreTurKnapp: ImageButton){
        if (!tur.status){
            insertTur(tur)
            tur.status = true
            Toast.makeText(activity , context?.getString(R.string.ny_tur_favoritter), Toast.LENGTH_SHORT).show()
            lagreTurKnapp.setColorFilter(Color.YELLOW)
        }
        else if(tur.status) {
            if (tur.mineTurer) {
                tur.status = false
                deleteTur(tur)
                Toast.makeText(activity, context?.getString(R.string.favoritt_slettet),Toast.LENGTH_SHORT).show()

                activity?.supportFragmentManager?.popBackStack()
                lagreTurKnapp.setColorFilter(Color.TRANSPARENT)
            } else{
                tur.status = false
                deleteTur(tur)

                Toast.makeText(
                    activity, context?.getString(R.string.favoritt_slettet),
                    Toast.LENGTH_SHORT
                ).show()

                lagreTurKnapp.setColorFilter(Color.TRANSPARENT)
            }
        }

        else{
            Toast.makeText(activity , context?.getString(R.string.feil_lagre_turer), Toast.LENGTH_SHORT).show()

        }
    }

    //Endrer og forsikrer oss om at fargen på stjernen er riktig opp mot favoritt database
    private fun endreFargeStjerne(favorittListe : List<Tur>, lagreTurKnapp: ImageButton){
        for (i in favorittListe){
            if (i.navn == tur.navn){
                tur.status = true
            }
        }
        if (tur.status){
            lagreTurKnapp.setColorFilter(Color.YELLOW)
        }
        else if(!tur.status){
            lagreTurKnapp.setColorFilter(Color.TRANSPARENT)
        }
    }

    private fun insertTur(tur : Tur){
        turInfoViewModel.insert(tur)
    }

    private fun deleteTur(tur: Tur) {
        turInfoViewModel.delete(tur)
    }
}
