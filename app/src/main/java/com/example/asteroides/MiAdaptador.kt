package com.example.asteroides

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MiAdaptador(context: Context, private val lista: List<String>) :
    RecyclerView.Adapter<MiAdaptador.ViewHolder>() {
    private val inflador: LayoutInflater

    init {
        inflador = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = inflador.inflate(R.layout.elemento_lista, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.titulo.text = lista[i]
        when (Math.round(Math.random().toFloat() * 3)) {
            0 -> {holder.icono.setImageResource(R.drawable.asteroide1)}
            1 -> {holder.icono.setImageResource(R.drawable.asteroide2)}
            2 -> {holder.icono.setImageResource(R.drawable.asteroide3)}
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titulo: TextView
        var subtitulo: TextView
        var icono: ImageView

        init {
            titulo = itemView.findViewById(R.id.titulo)
            subtitulo = itemView.findViewById(R.id.subtitulo)
            icono = itemView.findViewById(R.id.icono)
        }
    }
}