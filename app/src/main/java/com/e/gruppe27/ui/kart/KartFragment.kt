package com.e.gruppe27.ui.kart

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.e.gruppe27.R
import com.e.gruppe27.model.Tur
import com.esri.arcgisruntime.layers.WmsLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView


class KartFragment :  Fragment() {
	private lateinit var mGraphicsOverlay: GraphicsOverlay
	private lateinit var kartViewModel: KartViewModel
	private lateinit var mMapView: MapView
	private lateinit var turListe: List<Tur>
	private lateinit var mWmsBrattLayer: WmsLayer
	private lateinit var mWmsTopoLayer: WmsLayer
	private lateinit var mapitem: ArcGISMap
	private var tur: Tur? = null

    override fun onCreateView (
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		kartViewModel = ViewModelProvider(this).get(KartViewModel::class.java)
		var root  = inflater.inflate(R.layout.fragment_kart, container, false)

		if (isConnected()){

				mMapView = root.findViewById(R.id.kartView)
				mapitem = kartViewModel.setupMap()
				mWmsTopoLayer = kartViewModel.getWmsTopoLayers()
				addBrattWms(mapitem)
				mMapView.map = mapitem
				toggleButtons(root)
				turListe = kartViewModel.getTurListe()
				createGraphicsOverlay()

				if (tur != null) {
					kartViewModel.visTurPaaKart(tur!!, mMapView)
					kartViewModel.createGraphicsOverlay(mMapView)
					//createPolylineGraphics(tur)
				} else {
					kartViewModel.visAlleTurerPaaKartet(turListe, mMapView)
				}

		}
		else{

				root = inflater.inflate(R.layout.no_internett_layout_fragment, container, false)

		}


		return root

	}

	// legger til turen vi er inne i i turInfo-fragment
	fun settTur(t: Tur) {
		tur = t
	}

	//Lager overlay og setter det på kartet
	private fun createGraphicsOverlay() {
		mGraphicsOverlay = GraphicsOverlay()
		mMapView.graphicsOverlays.add(mGraphicsOverlay)
	}

	//Setter funksjon til switch for valg av layers på kart
    private fun toggleButtons(root: View) {
        val velgerBratt: Switch = root.findViewById(R.id.layerBrattVelger)
        val velgerTopo: Switch = root.findViewById(R.id.layerTopoVelger)

		//Topo layer er o% transparent så det må skrus av og på for å få bratthet på toppen
        velgerBratt.setOnClickListener {
            if (mWmsTopoLayer.isVisible and velgerBratt.isChecked) {
                mWmsTopoLayer.isVisible = false
                mWmsBrattLayer.isVisible = velgerBratt.isChecked
                mWmsTopoLayer.isVisible = true
            } else {
                mWmsBrattLayer.isVisible = velgerBratt.isChecked
            }
        }
        velgerTopo.setOnClickListener {
            mWmsTopoLayer.isVisible = velgerTopo.isChecked
        }
    }

	//Legger bratthet layer på kartet
    private fun addBrattWms(map: ArcGISMap) {
        val wmsLayerNames: MutableList<String> =
        mutableListOf(getString(R.string.bratthet_layer1))
        mWmsBrattLayer = WmsLayer(getString(R.string.wms_url_bratthet), wmsLayerNames)
        mWmsBrattLayer.isVisible = false
        mWmsBrattLayer.brightness = 50.0F
        map.operationalLayers.add(mWmsBrattLayer)
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

}
