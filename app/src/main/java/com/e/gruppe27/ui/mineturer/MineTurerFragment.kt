package com.e.gruppe27.ui.mineturer

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.gruppe27.R
import com.e.gruppe27.model.Tur
import com.e.gruppe27.ui.utforskturer.TurAdapter
import com.e.gruppe27.ui.utforskturer.OnTurItemClickListener
import com.e.gruppe27.ui.turinfo.AppbarLayoutFragment

//Tag for backstack
private const val NAV_HOST_TAG = "TUR_FRAGMENT"

class MineTurerFragment : Fragment(), OnTurItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var turListe : List<Tur>
    private lateinit var dialogItems : Array<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var root = inflater.inflate(R.layout.fragment_mineturer, container, false)
        val mineTurerViewModel = ViewModelProvider(this).get(MineTurerViewModel::class.java)

        if (isConnected()){
            val click : OnTurItemClickListener = this
            val dialog : ImageButton = root.findViewById(R.id.dialog_opener)
            dialogItems = resources.getStringArray(R.array.dialog_items)


            mineTurerViewModel.alleTurer.observe(viewLifecycleOwner, Observer {
                turListe = it
                settRecyclerView(it, click)
            })
            dialog.setOnClickListener{
                settDialog()
            }
        }else{
            root = inflater.inflate(R.layout.no_internett_layout_fragment,container,false)
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


    //Setter recyclerview fra Turdatabase
    private fun settRecyclerView(turListe: List<Tur>, click : OnTurItemClickListener) {
        viewManager = LinearLayoutManager(activity)
        viewAdapter = TurAdapter(turListe, click)
        recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerViewMineTurer).apply {
            // setter status på at tur
        for (tur in turListe){
            tur.status = true
            tur.mineTurer = true
        }
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    //Åpner og styrer dialogboks
    private fun settDialog(){
        if (turListe.isNotEmpty()) {
            val nyDialog = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
            val valg = dialogItems

            nyDialog.setSingleChoiceItems(
                valg,
                -1
            ) { _: DialogInterface?, which: Int ->

                turListe  = when(which){
                    0 -> { turListe.sortedBy { it.lengde }}
                    1-> {turListe.sortedBy { it.gradering.length }}
                    2 -> {turListe.sortedBy { it.omrade }}
                    else -> {turListe}
                }

                settRecyclerView(turListe, this)

            }
            nyDialog.setTitle(context?.getString(R.string.sorter_etter))
            nyDialog.setNegativeButton(context?.getString(R.string.lukk), null)
            nyDialog.create().show()
        }
        else{
            Toast.makeText(activity , context?.getString(R.string.ingen_sortering), Toast.LENGTH_LONG).show()

        }
    }

        //Legger til fragmentet this i backstack når videre navigering forekommer
        override fun onItemClick(item: Tur, position: Int) {

            val turInfo = AppbarLayoutFragment(item)
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, turInfo, NAV_HOST_TAG)
            transaction.addToBackStack(null)

            transaction.commit()
        }
}
