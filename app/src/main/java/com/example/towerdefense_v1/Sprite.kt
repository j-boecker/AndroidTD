package com.example.towerdefense_v1

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Debug
import android.provider.SyncStateContract
import android.util.Log

abstract class Sprite(val images: Map<String, Bitmap>, var x: Float, var y: Float) {


    var w: Int = 0
    var h: Int = 0
    var xPixScale = Resources.getSystem().displayMetrics.widthPixels / GConst.GRIDWIDTH
    var yPixScale = Resources.getSystem().displayMetrics.heightPixels / GConst.GRIDHEIGHT

    init {
    }

    abstract fun draw(canvas: Canvas)

    abstract fun update()
}