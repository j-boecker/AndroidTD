package com.example.towerdefense_v1

import android.graphics.Bitmap
import android.graphics.Canvas

class TowerArcher(images: Map<String, Bitmap>, x: Float, y: Float) : Tower(images, x, y) {

    var animTimer = 0
    var idle = true


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (idle) {
            when(animTimer)
            {
                in 0..49 ->  canvas.drawBitmap((images[GConst.IDLE_IMAGE1]) as Bitmap, x * xPixScale, y * yPixScale, null)
                in 50..99->   canvas.drawBitmap(images[GConst.IDLE_IMAGE2] as Bitmap , x * xPixScale, y * yPixScale, null)
                100 -> { canvas.drawBitmap(images[GConst.IDLE_IMAGE2] as Bitmap , x * xPixScale, y * yPixScale, null)
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