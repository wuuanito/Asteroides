package com.example.asteroides

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View

class Grafico(var view: View, var drawable: Drawable) {
    var cenX = 0
    var cenY = 0 // Representa las coordenadas del centro
    var ancho: Int
    var alto: Int
    var incX = 0.0
    var incY = 0.0 // Incremento en el eje X e Y
    var angulo = 0.0
    var rotacion = 0.0
    var radioColision: Int
    var xAnterior = 0
    var yAnterior = 0
    var radioInval: Int

    init {
        ancho = drawable.intrinsicWidth
        alto = drawable.intrinsicHeight
        radioColision = (alto + ancho) / 4
        radioInval = Math.hypot((ancho / 2).toDouble(), (alto / 2).toDouble()).toInt()
    }

    fun dibujarGrafico(canvas: Canvas) {
        val x = cenX - ancho / 2
        val y = cenY - ancho / 2
        drawable.setBounds(x, y, x + ancho, y + alto)
        canvas.save()
        canvas.rotate(angulo.toFloat(), cenX.toFloat(), cenY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
        xAnterior = cenX
        yAnterior = cenY
    }

    fun incrementarPos(factor: Double) {
        cenX += (incX * factor).toInt()
        cenY += (incY * factor).toInt()
        angulo += rotacion * factor
        if (cenX < 0) cenX = view.width
        if (cenX > view.width) cenX = 0
        if (cenY < 0) cenY = view.height
        if (cenY > view.height) view.height
        view.postInvalidate(
            cenX - radioInval,
            cenY - radioInval,
            cenX + radioInval,
            cenY + radioInval
        )
        view.postInvalidate(
            xAnterior - radioInval,
            yAnterior - radioInval,
            xAnterior + radioInval,
            yAnterior + radioInval
        )
    }

    fun distancia(g: Grafico): Double {
        return Math.hypot((cenX - g.cenX).toDouble(), (cenY - g.cenY).toDouble())
    }

    fun verificaColision(g: Grafico): Boolean {
        return distancia(g) < radioColision + g.radioColision
    }

    fun incrementaPos(toDouble: Double) {}
}