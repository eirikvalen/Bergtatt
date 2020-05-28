package com.e.gruppe27.ui.turinfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.e.gruppe27.R
import com.e.gruppe27.model.Tur
import com.e.gruppe27.model.skred.AvalancheResponseItem
import com.e.gruppe27.model.weather.LocationForecast
import com.e.gruppe27.utils.GPXparser
import kotlinx.android.synthetic.main.fragment_skredinfo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

private const val BASE_ICON_URL= "https://in2000-apiproxy.ifi.uio.no/weatherapi/weathericon/1.1/?content_type=image%2Fpng&symbol="
private const val API_LANGUAGE = "1"
//Dangerlevels Avalanche API
private const val LOW_DANGER = "1"
private const val MEDIUM_DANGER = "2"
private const val HIGH_DANGER = "3"
private const val VERY_HIGH_DANGER = "4"
private const val EXTREME_DANGER = "5"

private const val NO_DATA_FOUND = "Ingen data tilgjengelig.."
//Date formatting
private const val USED_FORMAT_SHORT = "yyyy-mm-dd"
private const val USED_FORMAT_LONG = "yyyy-MM-dd'T'HH:mm:ss'Z'"


class SkredInfoFragment(val tur : Tur) : Fragment() {

    private lateinit var viewModel: TurInfoViewModel
    private lateinit var weatherViewModel: LocationForecastViewModel
    private val dayRange  = (8..9)
    private val monthRange = (5..6)
    private val yearRange = (0..3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_skredinfo, container, false)
        viewModel = ViewModelProvider(this).get(TurInfoViewModel::class.java)

        weatherViewModel = ViewModelProvider(this).get(LocationForecastViewModel::class.java)

        settData()

        return view
    }


    private fun settData() = CoroutineScope(Dispatchers.IO).launch {
        val locationList = GPXparser().decodeGPX(tur.gpxSrc)
        val lat = locationList[0].latitude
        val long = locationList[0].longitude

        val datoIdag = getDate()

        withContext(Dispatchers.Main) {
            viewModel.getAvalanche(lat, long, API_LANGUAGE, datoIdag, datoIdag)
            viewModel.avalancheData.observe(viewLifecycleOwner, Observer {
                setAvalancheData(it)
            })

            weatherViewModel.getWeather(lat, long)
            weatherViewModel.currentWeather.observe(viewLifecycleOwner, Observer { forecast ->
                setWeatherData(forecast)

            })
        }
    }

    //Setter data til view elementer basert på data fra API
    private fun setAvalancheData(avalancheResponseItem: AvalancheResponseItem) {
        val skredAdvice = avalancheResponseItem.avalancheAdvices

        try {
            sisteOppdatering.text = avalancheResponseItem.publishTime.formatPublishtime()
            region_felt.text = avalancheResponseItem.regionName
            skredProblemer.text = avalancheResponseItem.mainText

            //Gode tips i fjellet mtp forholdene
            skredTips.text = skredAdvice?.get(0)?.text
            utvidetInfo.text = avalancheResponseItem.avalancheDanger
            val iconview = skredRan

            when (avalancheResponseItem.dangerLevel) {
                LOW_DANGER -> iconview.setImageResource(R.drawable.ic_warning_1)
                MEDIUM_DANGER -> iconview.setImageResource(R.drawable.ic_warning_2)
                HIGH_DANGER -> iconview.setImageResource(R.drawable.ic_warning_3)
                VERY_HIGH_DANGER -> iconview.setImageResource(R.drawable.ic_warning_4)
                EXTREME_DANGER -> iconview.setImageResource(R.drawable.ic_warning_5)
                else -> iconview.setImageResource(R.drawable.ic_warning_null)
            }
        } catch (e: java.lang.NullPointerException) {
            sisteOppdatering.text = NO_DATA_FOUND
            region_felt.text = NO_DATA_FOUND
            skredProblemer.text = NO_DATA_FOUND
            skredTips.text = NO_DATA_FOUND
            utvidetInfo.text = NO_DATA_FOUND
            val iconview = skredRan
            iconview.setImageResource(R.drawable.ic_warning_null)
        }
    }


    //Metode som formaterer dato på formen yyyy-MM-dd'T'HH:mm:ss til dd.MM.YY
    private fun String.formatPublishtime() : String{
        val dag = this.substring(dayRange)
        val maaned = this.substring(monthRange)
        val aar = this.substring(yearRange)

        return "$dag.$maaned.$aar"
    }


    //Setter data til view elementer basert på WeatherAPI
    @SuppressLint("SimpleDateFormat")
    private fun setWeatherData(forecast : LocationForecast){
        val dateformatter = SimpleDateFormat(USED_FORMAT_LONG)

        val timeNow = Calendar.getInstance().time

        val cal  = Calendar.getInstance()
        cal.time = timeNow
        cal.add(Calendar.HOUR_OF_DAY, 1)
        val nowplusonehour = cal.time

        val weatherTimeList = forecast.product.time.filter {
            val fromstring = it.from
            val time : Date? = dateformatter.parse(fromstring)

            time?.after(timeNow)!! && time.before(nowplusonehour)
        }


        /*
        Værdata kan ofte være ustabil og null så forsikrer oss mor Nullpointer
        I responsen så er det tre objekter som har samme fra-tidstpunkt, som inneholder litt
        forskjellig data. Derfor velger vi fra weatherTimeList, de objektene vi vet har riktig data
         */
        try {
            val maksTempData = weatherTimeList[2].location.maxTemperature.value
            val minTempData = weatherTimeList[2].location.minTemperature.value
            val hastighetVind = weatherTimeList[0].location.windSpeed.mps
            val vindNavn = weatherTimeList[0].location.windSpeed.name
            val orientVind = weatherTimeList[0].location.windDirection.name
            val taake = weatherTimeList[0].location.fog.percent


            temperatur.text = getString(R.string.temp_min_max, minTempData, maksTempData)
            windContainer.text = getString(R.string.vindinfo, vindNavn, orientVind, hastighetVind)

            val sikt = String.format( "%.0f" , (100.0 - taake.toDouble()))
            siktContainer.text = getString(R.string.siktinfo, sikt)


        } catch (e: NullPointerException) {

            temperatur.text = context?.getString(R.string.ingen_data)
            windContainer.text = context?.getString(R.string.ingen_data)
            siktContainer.text = context?.getString(R.string.ingen_data)
        }

        val iconNumber = weatherTimeList[1].location.symbol.number

        val iconUrl = BASE_ICON_URL + iconNumber
        Glide.with(this@SkredInfoFragment).load(iconUrl).into(vaerInfo)
    }


    @SuppressLint("SimpleDateFormat")
    private fun getDate() : String{
        val hentDato: Date = Calendar.getInstance().time
        val onsketFormat = SimpleDateFormat(USED_FORMAT_SHORT)
        return onsketFormat.format(hentDato)
    }

}