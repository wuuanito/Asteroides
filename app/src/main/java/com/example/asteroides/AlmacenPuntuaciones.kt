package com.example.asteroides

import android.content.Context

class AlmacenPuntuaciones(context: Context) {

    val context = context
    fun guardarPuntuacion(nPuntos: Int, nNombre:String){
        val preferencias = context.getSharedPreferences("puntuaciones", Context.MODE_PRIVATE)
        val editor = preferencias.edit()

        for (n in 0 until 10){
            val puntos= preferencias.getString("puntuacion$n","0-Pepe")?.split("-")?.get(0)?.toInt() ?:0
            val editor = preferencias.edit()
            if (nPuntos >= puntos)
            {
                for (i in 8 downTo n){
                    editor.putString("puntuacion${n+1}",preferencias.getString("puntuacion$i",""))
                }
            }
        }

    }
    fun listarPuntuaciones(): List<String>{

        val result = ArrayList<String>()
        val preferencias = context.getSharedPreferences("puntuaciones", Context.MODE_PRIVATE)
        for (n in 0 until 10)
        {
            val s = preferencias.getString("puntuacion$n","")
            if (s !=""){
                result.add(s!!)
            }
        }

        return result

    }





}