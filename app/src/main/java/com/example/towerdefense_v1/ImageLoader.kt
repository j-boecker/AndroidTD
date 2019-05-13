package com.example.towerdefense_v1

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class ImageLoader {
    var imagesArcher : MutableMap<String, Bitmap> = mutableMapOf()
    var imagesCreep : MutableMap<String, Bitmap> = mutableMapOf()
    var ctx = ApplicationContextProvider.applicationContext()

    init {
        imagesCreep.put(GConst.CREEP_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.creepright1))

        imagesArcher.put(GConst.IDLE_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe1))
        imagesArcher.put(GConst.IDLE_IMAGE2,  BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe2))
        imagesArcher.put(GConst.PROJECTILE_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_arrow))
        imagesArcher.put(GConst.SHADOW_IMAGE, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shadow))
        imagesArcher.put(GConst.SHOOT_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot1))
        imagesArcher.put(GConst.SHOOT_IMAGE2, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot2))
        imagesArcher.put(GConst.SHOOT_IMAGE3, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot3))
    }
}