package com.example.towerdefense_v1

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Debug
import android.provider.SyncStateContract
import android.util.Log

abstract class Sprite(val images: Map<String, Bitmap>, var x: Float, var y: Float) {

    var width = 50f
    var height = 50f

    protected var xFloat: Float = 0f
    protected var yFloat: Float = 0f
    lateinit var currentBitmap: Bitmap

    var isDisposable = false

    var xPixScale = Resources.getSystem().displayMetrics.widthPixels / GConst.GRIDWIDTH
    var yPixScale = Resources.getSystem().displayMetrics.heightPixels / GConst.GRIDHEIGHT

    init {
    }

    fun collidesWith(sprite: Sprite): Boolean {
        return (x <= sprite.x + sprite.width && x + width >= sprite.x && y <= sprite.y + sprite.height && y + height >= sprite.y)
    }

    fun move(distanceX: Float, distanceY: Float) {
        //save method to exceed grid boundaries with moving
        x = Math.min(Math.max(0f, x + distanceX), GConst.GRIDWIDTH.toFloat())
        y = Math.min(Math.max(0f, x + distanceY), GConst.GRIDWIDTH.toFloat())
    }

    abstract fun draw(canvas: Canvas)

    abstract fun update()


}