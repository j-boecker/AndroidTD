package com.example.towerdefense_v1

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix


class Creep(images: Map<String, Bitmap>, x : Float, y : Float, var maxHp: Float, var speed: Float) : Sprite(images,x,y) {

    var matrix: Matrix = Matrix()
    init {
        matrix.setTranslate(x * xPixScale*2f,y * yPixScale*2f)
        matrix.postScale(0.5f,0.5f)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap((images[GConst.CREEP_IMAGE1]) as Bitmap, matrix, null)
    }

    override fun update() {
        move()
    }

    fun move(){
        x+=speed
        matrix.setTranslate(x * xPixScale*2f,y * yPixScale*2f)
        matrix.postScale(0.5f,0.5f)
    }
}