package com.example.asteroides

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PuntuacionesActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.puntuaciones)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adaptador = MiAdaptador(this,MainActivity.almacen.listarPuntuaciones())
        recyclerView.adapter = adaptador
        val layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
    }
}