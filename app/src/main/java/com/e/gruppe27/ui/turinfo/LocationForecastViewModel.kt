package com.e.gruppe27.ui.turinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.gruppe27.model.weather.LocationForecast
import kotlinx.coroutines.launch

class LocationForecastViewModel : ViewModel(){

    private val repository = LocationForecastRepository()
    private val _currentWeather = MutableLiveData<LocationForecast>()

    val currentWeather : LiveData<LocationForecast>
            get() = _currentWeather

    fun getWeather(lat : Double, long : Double) = viewModelScope.launch {
        val weather = repository.getCurrentWeather(lat,long)
        _currentWeather.postValue(weather)

    }




}