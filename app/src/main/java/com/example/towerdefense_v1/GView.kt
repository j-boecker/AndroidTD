package com.example.towerdefense_v1

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast

class GView(ctx: Context) : SurfaceView(ctx), SurfaceHolder.Callback {


    val backPaint = Paint()
    private var imagesArcher : MutableMap <String, Bitmap> = mutableMapOf()
    var tower1 : Tower
    var tower2 : Tower
   var towerBuildGrid : Array<Array<Tower?>>

    val gridWidth: Int = 10
    val gridHeight: Int = 10

    private val thread: GameThread
    init {

        backPaint.setColor(Color.RED)
        // putting the images in a map containing all bitmaps. Here a String Constant resource could be used to avoid spelling mistakes
        imagesArcher.put(GConst.IDLE_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe1))
        imagesArcher.put(GConst.IDLE_IMAGE2, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe2))
        imagesArcher.put(GConst.PROJECTILE_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_arrow))
        imagesArcher.put(GConst.SHADOW_IMAGE, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shadow))
        imagesArcher.put(GConst.SHOOT_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot1))
        imagesArcher.put(GConst.SHOOT_IMAGE2, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot2))
        imagesArcher.put(GConst.SHOOT_IMAGE3, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot3))

        //making a 2d array tile grid in kotlin is terrible
        towerBuildGrid = Array(GConst.TOWERGRIDWIDTH) {Array<Tower?>(GConst.TOWERGRIDHEIGHT){null}}


        tower1 = TowerArcher(imagesArcher,50f,50f)
        tower2 = TowerArcher(imagesArcher,400f,400f)
        towerBuildGrid[5][5] = tower2
        holder.addCallback(this)
        thread = GameThread(holder, this)

    }

    override fun surfaceCreated(holder: SurfaceHolder) {

        thread.setRunning(true)
        thread.start()
        Toast.makeText(this@GView.context,"version 1.2" + Resources.getSystem().displayMetrics.widthPixels + "  Y  " +  Resources.getSystem().displayMetrics.heightPixels, Toast.LENGTH_LONG).show()

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            retry = false
        }
    }

    fun update() {
        for( tList in towerBuildGrid){
            for(tw in tList){
                tw?.update()
            }
        }
    }

    //using override draw. Dont know if a new method like drawScreen() would be better
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        for( tList in towerBuildGrid){
            for(tw in tList){
                    tw?.draw(canvas)
            }
        }
    }
}