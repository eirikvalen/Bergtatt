package com.e.gruppe27

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener


class WelcomePage : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var myViewPagerAdapter: LandingPage
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: Array<TextView?>
    private lateinit var layouts: IntArray
    private lateinit var btnNext: Button
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        prefManager = PrefManager()
        prefManager.prefManager(this)
        if (prefManager.isFirstTimeLaunch()) {
            launchHomeScreen()
            finish()
        }

        setContentView(R.layout.welcome_page)

        viewPager =  findViewById(R.id.view_pager)
        dotsLayout = findViewById(R.id.layoutDots)
        btnNext = findViewById(R.id.btn_next)

        layouts = intArrayOf(R.layout.landingpage_1, R.layout.landingpage_2,R.layout.landingpage_3)

        addBottomDots(0)

        myViewPagerAdapter = LandingPage(this, layouts)
        viewPager.adapter = myViewPagerAdapter
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)



        btnNext.setOnClickListener {
            // checking for last page
            // if last page home screen will be launched
            val current = getItem()+1
            if (current < layouts.size) { // move to next screen
                viewPager.currentItem = current
            } else {
                launchHomeScreen()
            }
        }

    }

    //Legger til dots nederst på landingpage for å visualisere fremgang
    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)
        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226;")
            dots[i]?.textSize = 35F
            dots[i]?.setTextColor(getColor(R.color.dots_innaktiv))
            dotsLayout.addView(dots[i])
        }
        if (dots.isNotEmpty()) dots[currentPage]?.setTextColor(getColor(R.color.dots_aktivert))
    }

    private fun getItem(): Int {
        return viewPager.currentItem
    }

    //Starter activitet
    private fun launchHomeScreen() {
        prefManager.setFirstTimeLaunch(true)
        startActivity(Intent(this, MainActivity::class.java))
    }

    //Endrer tekst på side basert på sidetall
    private var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            // Bytter tekst fra Neste til Ferdig
            if (position == layouts.size - 1) { // Siste side. teksten settes til ferdig
                btnNext.text = getString(R.string.landingP_gotIt)
            } else { // når det fortsatt er sider tilgjenglig
                btnNext.text = getString(R.string.landingP_neste)
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    class LandingPage(context: Context, private val layouts :IntArray) : PagerAdapter() {
        private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }
        override fun getCount(): Int {
            return layouts.size
        }
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view: View = inflater.inflate(layouts[position], container, false)
            container.addView(view)
            return view
        }
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view: View = `object` as View
            container.removeView(view)
        }
    }
}