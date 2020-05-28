package com.e.gruppe27.ui


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.e.gruppe27.model.Tur
import com.e.gruppe27.ui.kart.KartFragment
import com.e.gruppe27.ui.turinfo.SkredInfoFragment
import com.e.gruppe27.ui.turinfo.TurInfoFragment

private const val NUM_TABS = 3

class MyPagerAdapter(fa: Fragment, var tur : Tur) : FragmentStateAdapter(fa){

    //Henter antall tabs
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    //Velger hvilket fragment som vises ved trykk på tabs
    override fun createFragment(position: Int): Fragment {
        val kartfragment = KartFragment()
        kartfragment.settTur(tur)
        return when (position) {
            0 -> TurInfoFragment(tur)
            1 -> SkredInfoFragment(tur)
            else -> kartfragment
        }
    }
}







