package com.e.gruppe27

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        supportActionBar?.hide()

        prefManager = PrefManager()
        prefManager.prefManager(this)

        val bergattLogo : ImageView = findViewById(R.id.splashBilde)
        val bergtattTittel: TextView = findViewById(R.id.splashTekst)
        val animasjon : Animation = AnimationUtils.loadAnimation(applicationContext,R.anim.fade)

        bergattLogo.startAnimation(animasjon)
        bergtattTittel.startAnimation(animasjon)

        handler = Handler()
        handler.postDelayed({

            //Sjekker om landingpage har blitt vist før
            if(prefManager.isFirstTimeLaunch()){
                val mainActivity = Intent(this,MainActivity::class.java)
                startActivity(mainActivity)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }else{
                val landingPage = Intent(this,WelcomePage::class.java)
                startActivity(landingPage)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }

        //Duration av hele splashscreen
        }, 3000)
    }
}