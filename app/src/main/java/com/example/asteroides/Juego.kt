package com.example.asteroides

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Juego : AppCompatActivity() {
    private lateinit var vistajuego : VistaJuego
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.juego)
        vistajuego = findViewById(R.id.VistaJuego)
        vistajuego.padre = this

    }


    override fun onPause() {
        vistajuego.threadJuego.pausar()
        super.onPause()
    }

    override fun onResume() {

        vistajuego.threadJuego.reanudar()
        super.onResume()
    }

    override fun onDestroy() {
        vistajuego.threadJuego.detener()
        super.onDestroy()
    }
}