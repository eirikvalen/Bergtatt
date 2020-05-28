package com.e.gruppe27.ui.kart

/*
KILDER fra ArcGis

Polyline, point and poly graphics
    - https://developers.arcgis.com/labs/android/display-point-line-and-polygon-graphics/
Layers on 2D mao
    - https://developers.arcgis.com/labs/android/add-layers-to-a-map/
Simple 2D map
    - https://developers.arcgis.com/labs/android/create-a-starter-app/
 */


import android.app.Application
import android.graphics.Color
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import com.e.gruppe27.R
import com.e.gruppe27.model.Tur
import com.e.gruppe27.ui.utforskturer.TurRepository
import com.e.gruppe27.utils.GPXparser
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.PointCollection
import com.esri.arcgisruntime.geometry.Polyline
import com.esri.arcgisruntime.geometry.SpatialReferences
import com.esri.arcgisruntime.layers.WmsLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Disse variablene benyttes for å fokusere kartet på hele Norge
private const val START_LAT = 64.3153
private const val START_LONG = 12.3712
private const val START_DETAIL = 5

//Størrelse på grafiske objekter
private const val STR_START_SLUTT = 12.0f
private const val STR_NORM_POINT = 8.0f
private const val LINE_WIDTH = 1.5f

//Map Zoom - duration og stopAtScale
private const val DURATION_ZOOM = 4f
private const val MAP_SCALE = 100000.0

class KartViewModel(val app: Application) : AndroidViewModel(app) {

    private lateinit var mGraphicsOverlay: GraphicsOverlay

    private val alleTurerRepository = TurRepository(app)

    private lateinit var mWmsTopoLayer: WmsLayer

    //Setter opp basiskartet med topografisk bunnkart
    fun setupMap(): ArcGISMap {
        val basemapType = Basemap.Type.TOPOGRAPHIC
        val latitude = START_LAT
        val longitude = START_LONG
        val levelOfDetail = START_DETAIL
        val map = ArcGISMap(basemapType, latitude, longitude, levelOfDetail)
        addTopoWms(map)
        return map
    }

    //Henter liste med Tur-objekter
    fun getTurListe():List<Tur>{
        return alleTurerRepository.hentAlleTurer()
    }

    //returnerer wmsLayer topografisk
    fun getWmsTopoLayers() : WmsLayer {return mWmsTopoLayer}

    private fun addTopoWms(map: ArcGISMap) {

        val topoLayerName = app.applicationContext.getString(R.string.topografisk_layer1)
        val topoUrl = app.applicationContext.getString(R.string.wms_url_topografisk)

        val wmsLayerNames : MutableList<String> = mutableListOf(topoLayerName)

        mWmsTopoLayer = WmsLayer(topoUrl, wmsLayerNames)
        mWmsTopoLayer.isVisible = false
        map.operationalLayers.add(mWmsTopoLayer)
        map.loadAsync()
    }

    //Brukes når vi visualiserer en tur på utvidetInfo Kart
    fun visTurPaaKart(tur : Tur, mMapView: MapView) {
        createGraphicsOverlay(mMapView)
        //createPointGraphics(tur)
        createLineWithPointsGraphics(tur, mMapView)
        /*
        //LAGER LINJE, MEN FUNGERER BARE PÅ MAC
        createPolylineGraphics()
         */
    }

    //Viser alle turer som punkt på kartet
    fun visAlleTurerPaaKartet(turListe :List<Tur>, mMapView: MapView){
        createGraphicsOverlay(mMapView)
        if(turListe.isNotEmpty()) {
            println("deterikketurer")
            for (tur in turListe) {
                createPointGraphics(tur)
            }
        }


    }

    //Brukes for å lage et punkt på et kart - benyttes i flere metoder
    private fun createPointGraphics(tur: Tur) {
        CoroutineScope(Dispatchers.IO).launch {
            val locationList = GPXparser().decodeGPX(tur.gpxSrc)
            val x = locationList[0].longitude
            val y = locationList[0].latitude

            val point = Point(x, y, SpatialReferences.getWgs84())
            val pointSymbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,
                Color.BLUE, STR_NORM_POINT
            )
            val pointGraphic = Graphic(point, pointSymbol)
            mGraphicsOverlay.graphics.add(pointGraphic)
        }
    }
    //setter overlay på kart
    fun createGraphicsOverlay(mMapView : MapView) : MapView {
        mGraphicsOverlay = GraphicsOverlay()
        mMapView.graphicsOverlays.add(mGraphicsOverlay)
        return mMapView
    }


    //Lager en blå linje på kart basert på kordinater
    fun createPolylineGraphics(tur: Tur) {
        CoroutineScope(Dispatchers.IO).launch {
            val polylinePoints = PointCollection(SpatialReferences.getWgs84())
            val locationList = GPXparser().decodeGPX(tur.gpxSrc)
            for (i in locationList) {
                val x = i.longitude
                val y = i.latitude
                polylinePoints.add(
                    Point(x, y)
                )
                val polyline = Polyline(polylinePoints)
                val polylineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.DASH, Color.GRAY, LINE_WIDTH)
                val polylineGraphic = Graphic(polyline, polylineSymbol)
                mGraphicsOverlay.graphics.add(polylineGraphic)
            }
        }
    }

    //Lager linje med punkter basert på tur-objektet sin GPX koordinater
    private fun createLineWithPointsGraphics(tur: Tur, mMapView: MapView) {
        CoroutineScope(Dispatchers.IO).launch {
            val pointSymbol = SimpleMarkerSymbol(
                SimpleMarkerSymbol.Style.CIRCLE,
                Color.RED,
                STR_NORM_POINT
            )
            val pointSymbolFirst = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,
            Color.BLUE, STR_START_SLUTT)
            val pointSymbolLast =  SimpleMarkerSymbol(SimpleMarkerSymbol.Style.X,
            Color.BLUE, STR_START_SLUTT)

            val locationList = GPXparser().decodeGPX(tur.gpxSrc)
            for (location in locationList) {
                val x = location.longitude
                val y = location.latitude
                val point = Point(x, y, SpatialReferences.getWgs84())
                // val pointGraphic = Graphic(point, pointSymbol)
                // mGraphicsOverlay.graphics.add(pointGraphic)
                val pointGraphic = when (location) {
                    locationList.first() -> {
                        Graphic(point, pointSymbolFirst)
                    }
                    locationList.last() -> {
                        Graphic(point, pointSymbolLast)
                    }
                    else -> {
                        Graphic(point, pointSymbol)
                    }
                }

                mGraphicsOverlay.graphics.add(pointGraphic)
            }

            val viewpointLong = getElementMiddle(locationList).longitude
            val viewpointLat = getElementMiddle(locationList).latitude
            val point = Point(viewpointLong,viewpointLat, SpatialReferences.getWgs84())
            val viewpoint = Viewpoint(point, MAP_SCALE)
            mMapView.setViewpointAsync(viewpoint, DURATION_ZOOM)

        }
    }

    //Henter Objekt i midten av en liste
    private fun getElementMiddle(liste: List<Location>): Location {
        return liste[liste.size / 2]
    }


}



