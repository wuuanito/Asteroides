package com.example.asteroides

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    fun lanzarAcercaDe(view: View? = null) {
        val i = Intent(this, AcercaDeActivity::class.java)
        startActivity(i)
    }
}