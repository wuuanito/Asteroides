package com.example.asteroides

import android.app.Application


class Aplicacion : Application() {
    var saldo: Int = 0

    override fun onCreate() {
        super.onCreate()
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        saldo = pref.getInt("saldo_inicial", -1)
    }
}