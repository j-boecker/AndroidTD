package com.example.towerdefense_v1

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class ImageLoader {
    var imagesArcher: MutableMap<String, Bitmap> = mutableMapOf()
    var imagesCreep: MutableMap<String, Bitmap> = mutableMapOf()
    var ctx = ApplicationContextProvider.applicationContext()

    init {
        imagesCreep.put(GConst.CREEP_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.creepright1))
        imagesCreep.put(GConst.CREEP_IMAGE2, BitmapFactory.decodeResource(ctx.resources, R.drawable.minionright2))
        imagesCreep.put(GConst.CREEP_IMAGE3, BitmapFactory.decodeResource(ctx.resources, R.drawable.miniondown1))
        imagesCreep.put(GConst.CREEP_IMAGE4, BitmapFactory.decodeResource(ctx.resources, R.drawable.miniondown2))
        imagesCreep.put(GConst.CREEP_IMAGE5, BitmapFactory.decodeResource(ctx.resources, R.drawable.minionleft1))
        imagesCreep.put(GConst.CREEP_IMAGE6, BitmapFactory.decodeResource(ctx.resources, R.drawable.minionleft2))
        imagesCreep.put(GConst.CREEP_IMAGE7, BitmapFactory.decodeResource(ctx.resources, R.drawable.minionup1))
        imagesCreep.put(GConst.CREEP_IMAGE8, BitmapFactory.decodeResource(ctx.resources, R.drawable.minionup2))


        imagesArcher.put(GConst.IDLE_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe1))
        imagesArcher.put(GConst.IDLE_IMAGE2, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe2))
        imagesArcher.put(GConst.PROJECTILE_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_arrow))
        imagesArcher.put(GConst.SHADOW_IMAGE, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shadow))
        imagesArcher.put(GConst.SHOOT_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot1))
        imagesArcher.put(GConst.SHOOT_IMAGE2, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot2))
        imagesArcher.put(GConst.SHOOT_IMAGE3, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot3))
        imagesArcher.put(GConst.PROJECTILE_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_arrow))
    }
}