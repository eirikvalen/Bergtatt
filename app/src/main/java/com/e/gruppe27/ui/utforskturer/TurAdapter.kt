package com.e.gruppe27.ui.utforskturer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.gruppe27.R
import com.e.gruppe27.model.Tur
import kotlinx.android.synthetic.main.tur_element.view.*
import kotlinx.android.synthetic.main.tur_element.view.distanse
import kotlinx.android.synthetic.main.tur_element.view.gradering


class TurAdapter(private val turListe : List<Tur>, private val clickListener: OnTurItemClickListener) : RecyclerView.Adapter<TurAdapter.TurViewHolder>(){


   class TurViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
        fun initialize (item: Tur, action: OnTurItemClickListener) {
            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TurViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tur_element, parent, false)

        return TurViewHolder(view)
    }

    override fun getItemCount() = turListe.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TurViewHolder, position: Int) {
        val tur = turListe[position]

        holder.itemView.turnavn.text = tur.navn
        holder.itemView.gradering.text = tur.gradering
        holder.itemView.distanse.text = "${tur.lengde} km"
        holder.itemView.varighet_boks.text = tur.varighet
        holder.itemView.type_tur_boks.text = tur.type
        holder.itemView.turomrade.text = tur.omrade
        holder.initialize(tur, clickListener)

        val imgView = holder.itemView.imageview
        Glide.with(holder.itemView).load(tur.bildeSrc).into(imgView)

      
    }
}
    interface OnTurItemClickListener {

        fun onItemClick(item: Tur, position: Int)
    }
