package com.e.gruppe27.ui.utforskturer

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.gruppe27.R
import com.e.gruppe27.model.Tur
import com.e.gruppe27.ui.turinfo.AppbarLayoutFragment


class UtforskTurerFragment : Fragment(), OnTurItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var turListe: List<Tur>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var view = inflater.inflate(R.layout.fragment_utforskturer, container, false)
        val viewModel = ViewModelProvider(this).get(UtforskTurerViewModel::class.java)

        if (isConnected()){

                val click : OnTurItemClickListener = this
                val dialog : ImageButton = view.findViewById(R.id.dialog_opener)
                turListe = viewModel.hentAlleTurer()
                setRecyclerView(turListe, view)
                dialog.setOnClickListener {
                    settDialogBoks(turListe, click)
                }

        }else{

                view = inflater.inflate(R.layout.no_internett_layout_fragment, container, false)

        }

        return view
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
    //Setter sorterte turer inn i recyclerview
    private fun sorterTur(turListe : List<Tur>, click : OnTurItemClickListener){
        recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerView).apply {
            for (tur in turListe){
                tur.mineTurer = false
            }


            viewAdapter = TurAdapter(turListe, click)
            viewAdapter.notifyDataSetChanged()
            adapter = viewAdapter
        }
    }

    //Åpner dialog boks og sorterer basert på valg
    private fun settDialogBoks(turListe : List<Tur>, click : OnTurItemClickListener){
        val nyDialog = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
        val valg: Array<String> = arrayOf("Lengde", "Vanskelighetsgrad", "Område")

        nyDialog.setSingleChoiceItems(
            valg,
            -1
        ) { _: DialogInterface?, which: Int ->

            val sortertListe = when(which){
                0 -> turListe.sortedBy{it.lengde}
                1 -> turListe.sortedBy{it.gradering.length}
                2 -> turListe.sortedBy{it.omrade}
                else -> turListe
            }

            sorterTur(sortertListe, click)

        }
        nyDialog.setTitle(context?.getString(R.string.sorter_etter))
        nyDialog.setNegativeButton(context?.getString(R.string.lukk), null)
        nyDialog.create().show()
    }

    //Åpner utvidet informasjon om en tur ved trykk på cardview
    override fun onItemClick(item: Tur, position: Int){
        val turInfo = AppbarLayoutFragment(item)
        val transaction : FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, turInfo, "TUR_FRAGMENT")
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setRecyclerView(turListe: List<Tur>, view : View) {
        viewManager = LinearLayoutManager(activity)
        viewAdapter = TurAdapter(turListe, this)

        //setter recyclerview
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

}
