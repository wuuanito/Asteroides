package com.example.asteroides

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.preference.PreferenceManager
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin

class VistaJuego(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    //  VARIABLES DEL THREAD
    val threadJuego = ThreadJuego()
    val PERIODO_PROCESO = 50
    var UTLIMO_PROCESO = 0L

    //VARIABLES DE LA NAVE
    var giroNave: Int = 0
    var aceleracionNave: Double = 0.0
    val MAX_VELOCIDAD_NAVE = 20


    var prefs = PreferenceManager.getDefaultSharedPreferences(getContext())
    var drawableasteroide = arrayOf(
    AppCompatResources.getDrawable(context!!, R.drawable.asteroide1)!!,
    AppCompatResources.getDrawable(context!!, R.drawable.asteroide1)!!,
    AppCompatResources.getDrawable(context!!, R.drawable.asteroide1)!!
    )
    var drawableNave: Drawable
    var drawableMisil: Drawable

    var asteroides = ArrayList<(Grafico)>()
    var num_asteroides = 3
    var num_Fragmentos = 3
    val nave: Grafico

    //VARIABLES PANTALLA TACTIL
    var touchX = 0.0F
    var touchY = 0.0F

    //VARIABLES MISIL

    val PASO_VELOCIAD_MISIL = 500
    var disparo = false
    val tiempoMisiles = ArrayList<Float>()
    val misiles = ArrayList<Grafico>()
    val TIEMPO_VIDA_MAX_MISIL = 100

    var puntuacion = 0
    lateinit var padre: Activity
    val pref: SharedPreferences =PreferenceManager.getDefaultSharedPreferences(getContext())
    var numAsteroides= pref.getString("numeroAsteroide","5")!!.toInt()?:5
    var numFragmentos=pref.getString("numeroFragmento","3")!!.toInt()?:3


    init {
        MainActivity.almacen.guardarPuntuacion((1..10).random(),"")
        if (prefs.getString("tipoGraficos", "1").equals("0")) {


            val pathAsteroide = Path()
            pathAsteroide.moveTo(0.3F, 0.0F)
            pathAsteroide.lineTo(0.6F, 0.0F)
            pathAsteroide.lineTo(0.6F, 0.3F)
            pathAsteroide.lineTo(0.8F, 0.2F)
            pathAsteroide.lineTo(1.0F, 0.4F)
            pathAsteroide.lineTo(0.8F, 0.6F)
            pathAsteroide.lineTo(0.9F, 0.9F)
            pathAsteroide.lineTo(0.8F, 1.0F)
            pathAsteroide.lineTo(0.4F, 1.0F)
            pathAsteroide.lineTo(0.0F, 0.6F)
            pathAsteroide.lineTo(0.0F, 0.2F)
            pathAsteroide.lineTo(0.3F, 0.0F)

            for (i in 0 until 3
            ) {
                val dAsteroide = ShapeDrawable(PathShape(pathAsteroide, 1.0F, 1.0F))
                dAsteroide.paint.color = Color.WHITE
                dAsteroide.paint.style = Paint.Style.STROKE
                dAsteroide.intrinsicHeight =(50-i*14)
                dAsteroide.intrinsicWidth = (50-i*14)
                drawableasteroide[i] = dAsteroide


            }
            setBackgroundColor(Color.BLACK)
            val pathNave = Path()
            pathNave.lineTo(0.0F, 1.0F)
            pathNave.lineTo(1.0F, 0.5F)
            pathNave.lineTo(0.0F, 0.0F)


            val dNave = ShapeDrawable(PathShape(pathNave, 1.0F, 1.0F))
            dNave.paint.color = Color.WHITE
            dNave.paint.style = Paint.Style.STROKE
            dNave.intrinsicHeight = 15
            dNave.intrinsicWidth = 20


            drawableNave = dNave
            val pathMisil = Path()
            pathMisil.lineTo(0.0F, 0.5F)
            pathMisil.lineTo(1.0F, 0.5F)
            pathMisil.lineTo(1.0F, 0.4F)
            pathMisil.lineTo(0.0F, 0.4F)
            pathMisil.lineTo(0.0F, 0.5F)
            val dMisil = ShapeDrawable(PathShape(pathMisil, 1.0F, 1.0F))
            dMisil.paint.color = Color.RED
            dMisil.paint.style = Paint.Style.STROKE
            dMisil.intrinsicHeight = 15
            dMisil.intrinsicWidth = 3
            drawableMisil = dMisil


        } else {

            drawableNave = AppCompatResources.getDrawable(context!!, R.drawable.nave)!!
            drawableMisil = AppCompatResources.getDrawable(context!!, R.drawable.misil1)!!
        }
        nave = Grafico(this, drawableNave)
        for (i in 0 until num_asteroides) {
            val asteroide = Grafico(this, drawableasteroide[i%3])
            asteroide.incX = Math.random() * 4-2
            asteroide.incY = Math.random() * 4-2
            asteroide.angulo = Math.random() * 360
            asteroide.rotacion = Math.random() * 8 - 4
            asteroides.add(asteroide)
        }
    }

    override fun onSizeChanged(ancho: Int, alto: Int, ancho_ant: Int, alto_ant: Int) {
        super.onSizeChanged(ancho, alto, ancho_ant, alto_ant)
        nave.cenX = ancho / 2
        nave.cenY = alto / 2
        for (asteroide in asteroides) {
            do {
                asteroide.cenY = (Math.random() * ancho).toInt()
                asteroide.cenX = (Math.random() * alto).toInt()
            } while (asteroide.distancia(nave) < (ancho + alto) / 5)
        }
        UTLIMO_PROCESO = System.currentTimeMillis()
        threadJuego.start()
    }

    fun activaMisil() {
        var misil = Grafico(this, drawableMisil)
        misil.cenX = nave.cenX
        misil.cenY = nave.cenY
        misil.angulo = nave.angulo
        misil.incX = cos(Math.toRadians(misil.angulo) * PASO_VELOCIAD_MISIL)
        misil.incY = sin(Math.toRadians(misil.angulo) * PASO_VELOCIAD_MISIL)


        synchronized(misiles)
        {
            misiles.add(misil)
            tiempoMisiles.add(TIEMPO_VIDA_MAX_MISIL.toFloat())
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        nave.dibujarGrafico(canvas)
        synchronized(misiles)
        {
            for (asteroide in asteroides) {
                asteroide.dibujarGrafico(canvas)
            }
        }
        synchronized(asteroides) {
            for (i in misiles.indices) {
                misiles[i].dibujarGrafico(canvas)
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        super.onTouchEvent(event)
        var x = event.x
        var y = event.y

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                var deltaX = abs(x - touchX)
                var deltaY = abs(y - touchY)
                if (deltaY < 6 && deltaX > 6) {
                    giroNave = ((x - touchX) / 2).roundToInt()
                    disparo = false

                } else if (deltaX < 6 && deltaY > 6) {
                    aceleracionNave = ((touchY - y) / 25).toDouble()
                    disparo = false
                }
            }

            MotionEvent.ACTION_UP -> {
                if (disparo) {
                    activaMisil()
                }
                giroNave = 0
                aceleracionNave = 0.0

            }

            MotionEvent.ACTION_DOWN -> {
                disparo = true
            }
        }



        touchX = x
        touchY = y

        return true
    }

    fun actualizarFisica() {
        val ahora = System.currentTimeMillis()
        if (UTLIMO_PROCESO + PERIODO_PROCESO > ahora)
            return

        val factorMov = (ahora - UTLIMO_PROCESO) / PERIODO_PROCESO

        UTLIMO_PROCESO = ahora
        synchronized(nave) {
            nave.angulo += giroNave * factorMov
            val nuevoIncX =
                nave.incX + aceleracionNave * cos(Math.toRadians(nave.angulo.toDouble())) * factorMov
            val nuevoIncY =
                nave.incY + aceleracionNave * sin(Math.toRadians(nave.angulo.toDouble())) * factorMov

            if (hypot(nuevoIncX, nuevoIncY) <= MAX_VELOCIDAD_NAVE) {
                nave.incX = nuevoIncX
                nave.incY = nuevoIncY
            }

            nave.incrementarPos(factorMov.toDouble())
        }
        synchronized(asteroides)
        {
            for (asteroide in asteroides)
                asteroide.incrementarPos(factorMov.toDouble())
        }

        synchronized(misiles){
        for (i in misiles.indices.reversed()) {
            val misil = misiles[i]
            misil.incrementarPos(factorMov.toDouble())
            tiempoMisiles[i] -= factorMov.toFloat()
            if (tiempoMisiles[i] <= 0) {
                synchronized(misiles)
                {
                    misiles.removeAt(i)
                    tiempoMisiles.removeAt(i)
                }
            } else {
                for (i in asteroides.indices) {
                    if (misil.verificaColision(asteroides[i])) {
                        synchronized(misiles)
                        {
                            misiles.removeAt(i)
                            tiempoMisiles.removeAt(i)
                        }
                        synchronized(asteroides)
                        {
                            asteroides.removeAt(i)
                        }
                        break
                    }
                }
            }
        }
        }
        synchronized(asteroides)
        {
            for (asteroide in asteroides)
            {
                if (asteroide.verificaColision(nave))
                {
                    salir()
                }
            }
        }
    }

    inner class ThreadJuego: Thread()
    {
        private val lock=Object()
        private var pausa=false
        private var corriendo=true
        fun pausar(){
            synchronized(lock){
                pausa=true
            }
        }
        fun reanudar(){
            synchronized(lock){
                pausa=false
                lock.notify()
            }
        }
        fun detener(){
            synchronized(lock){
                corriendo=false
                if(pausa){
                    reanudar()
                }
            }
        }
        override fun run()
        {
            while(corriendo)
            {
                actualizarFisica()
                synchronized(lock){
                    while(pausa){
                        try{
                            lock.wait()
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }


    fun destruyeAsteroide(i: Int)
    {

        val asteroideAntiguo = asteroides[i]
        asteroides.removeAt(i)
        puntuacion += 1000
        var tam : Int
        if (asteroideAntiguo.drawable != drawableasteroide[2])
        {
            if (asteroideAntiguo.drawable != drawableasteroide[1]){
                tam = 2
            }
            else
            {
                tam = 1
            }
            for (i in 0 until num_Fragmentos)
            {
                val asteroide = Grafico(this,drawableasteroide[tam])
                asteroide.cenX=asteroideAntiguo.cenX
                asteroide.cenY = asteroideAntiguo.cenY
                asteroide.incX = (Math.random()*7-2-tam)
                asteroide.incY = (Math.random()*7-2-tam)
                asteroide.angulo = (Math.random()*360)
                asteroide.rotacion = (Math.random()*8-4)
                asteroides.add(asteroide)
            }
        }



    }


    fun salir ()
    {
        val bundle = Bundle()
        bundle.putInt("puntuaciones",puntuacion)
        padre.setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
        padre.finish()

    }
}