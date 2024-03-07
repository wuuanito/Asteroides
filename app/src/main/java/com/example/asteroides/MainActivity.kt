package com.example.asteroides

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {


    companion object
    {

        @SuppressLint("StaticFieldLeak")
        lateinit var almacen : AlmacenPuntuaciones


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        almacen= AlmacenPuntuaciones(this)

    }
    fun lanzarAcercaDe(view: View? = null) {
        val i = Intent(this, AcercaDeActivity::class.java)
        startActivity(i)
    }
    fun lanzarJuego(view: View? = null) {
        val i = Intent(this, Juego::class.java)
        startActivity(i)
    }
    fun lanzarPreferencias(view: View? = null) {
        val i = Intent(this, PreferenciasActivity::class.java)
        startActivity(i)
    }

    fun mostrarPreferencias(view: View? = null)
    {
        val prefs=PreferenceManager.getDefaultSharedPreferences(this  )
        val salida = "Musica: ${prefs.getBoolean("activarMusica",false)}"+
                "Modo Grafico: ${prefs.getString("tipoGraficos","none")}"+
        "Numero De Fragmentos: ${prefs.getString("numeroFragmentos","error")}"
        Toast.makeText(this,salida,Toast.LENGTH_LONG).show()
    }

    fun lanzarPuntuaciones(view: View?=null){
        val i = Intent(this, PuntuacionesActivity::class.java)
        startActivity(i)
    }
}