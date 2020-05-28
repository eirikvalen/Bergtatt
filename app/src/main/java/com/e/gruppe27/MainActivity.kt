package com.e.gruppe27


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.no_internet_layout.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        sjekkNett()
    }

    //Sjekker om det er internett tilgjengelig. Handler etter status
    private fun sjekkNett(){
        if (isConnected()){
            setContentView(R.layout.activity_main)
            val fragmentTurManager = supportFragmentManager
            val navHostFragment =
                fragmentTurManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            //Setter opp bottom navigation bar
            val navView: BottomNavigationView = findViewById(R.id.bottom_nav)
            val navController = navHostFragment.navController
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_skredvalsel,
                    R.id.navigation_alleTurer,
                    R.id.navigation_kart,
                    R.id.navigation_mineTurer
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
        //Telefonen er ikke på nett og en spesifikk layout vises
        else{
            setContentView(R.layout.no_internet_layout)

            testNett.setOnClickListener{
                onClick()
            }
        }
    }

    //Sjekker om telefonen er koblet opp mot internett
    private fun isConnected(): Boolean {
        var connected = false
        try {
            val cm =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {}
        return connected
    }

    //Refresher mainActivity for å sjekke om nett har blitt skrudd på
    private fun onClick() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    //Disabler hjemknapp
    override fun onBackPressed(){}
}


