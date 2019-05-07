package com.example.towerdefense_v1

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

class TowerArcher(images: Map<String, Bitmap>, x: Float, y: Float) : Tower(images, x, y) {

    var animTimer = 0
    var idle = true
    var matrix: Matrix = Matrix()

    init {

        matrix.setTranslate(x * xPixScale*2f,y * yPixScale*2f)
        matrix.postScale(0.5f,0.5f)

    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (idle) {
            when(animTimer)
            {
                in 0..49 ->  canvas.drawBitmap((images[GConst.IDLE_IMAGE1]) as Bitmap, matrix, null)
                in 50..99->   canvas.drawBitmap(images[GConst.IDLE_IMAGE2] as Bitmap , matrix, null)
                100 -> { canvas.drawBitmap(images[GConst.IDLE_IMAGE2] as Bitmap , matrix, null)
                         animTimer = 0
                        }
            }
        }
    }

    override fun update() {
        super.update()
        animTimer++
    }
}