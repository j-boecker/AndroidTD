package com.example.towerdefense_v1

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix

class TowerArcher(images: Map<String, Bitmap>, spriteList: MutableList<Sprite>,
                  creepList: MutableList<Creep>, x: Float, y: Float) : Tower(images, x, y,10f,100f,spriteList,creepList) {

    var animTimer = 0
    var idle = true
    var matrix: Matrix = Matrix()
    var currentBitmap = images[GConst.IDLE_IMAGE1] as Bitmap

    init {
        matrix.setTranslate(x * xPixScale*2f,y * yPixScale*2f)
        matrix.postScale(0.5f,0.5f)
        Globaltwo.x = 2
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawBitmap(currentBitmap, matrix, null)
    }

    override fun update() {
        super.update()
        animTimer++

        if (idle) {
            when(animTimer)
            {
                in 0..49 ->  currentBitmap = images[GConst.IDLE_IMAGE1] as Bitmap
                in 50..99->   currentBitmap = images[GConst.IDLE_IMAGE2] as Bitmap
                100 -> { currentBitmap = images[GConst.IDLE_IMAGE1] as Bitmap
                    animTimer = 0
                }
            }
        }
    }
}