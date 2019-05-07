package com.example.towerdefense_v1

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast

class GView(ctx: Context) : SurfaceView(ctx), SurfaceHolder.Callback {


    val backPaint = Paint()
    var imagesArcher : MutableMap<String, Bitmap> = mutableMapOf()
    var imagesCreep : MutableMap<String, Bitmap> = mutableMapOf()
    private val thread: GameThread
    val imageGrass: Bitmap
    val paintBig = Paint()
    val paintLines = Paint()
    var screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
    var screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()

    init {

        paintLines.color = Color.LTGRAY
        //paintLines.alpha = 100

        paintBig.isAntiAlias = true
        paintBig.isFilterBitmap = true
        paintBig.isDither = true
        backPaint.setColor(Color.RED)
        // putting the images in a map containing all bitmaps. Here a String Constant resource could be used to avoid spelling mistakes
        imagesCreep.put(GConst.CREEP_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.creepright1))

        imagesArcher.put(GConst.IDLE_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe1))
        imagesArcher.put(GConst.IDLE_IMAGE2,  BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe2))
        imagesArcher.put(GConst.PROJECTILE_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_arrow))
        imagesArcher.put(GConst.SHADOW_IMAGE, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shadow))
        imagesArcher.put(GConst.SHOOT_IMAGE1, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot1))
        imagesArcher.put(GConst.SHOOT_IMAGE2, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot2))
        imagesArcher.put(GConst.SHOOT_IMAGE3, BitmapFactory.decodeResource(ctx.resources, R.drawable.ashe_shoot3))
        imageGrass = BitmapFactory.decodeResource(ctx.resources, R.drawable.grass)

        holder.addCallback(this)
        thread = GameThread(holder, this)

    }

    override fun surfaceCreated(holder: SurfaceHolder) {

        thread.setRunning(true)
        thread.start()
        Toast.makeText(
            this@GView.context,
            "v1.3 -- X: " + Resources.getSystem().displayMetrics.widthPixels + " Y: " + Resources.getSystem().displayMetrics.heightPixels,
            Toast.LENGTH_LONG
        ).show()

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


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
        screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
        if(screenHeight<screenWidth)
            screenWidth = screenWidth*0.8f
        else
            screenHeight = screenHeight*0.8f

        canvas?.drawBitmap(imageGrass, 0f, 0f, paintBig)
        for (i in 1..GConst.TOWERGRIDWIDTH){
            canvas?.drawLine(i*(screenWidth/GConst.TOWERGRIDWIDTH),0f,i*(screenWidth/GConst.TOWERGRIDWIDTH),screenHeight,paintLines)
        }
        for (i in 1..GConst.TOWERGRIDHEIGHT){
            canvas?.drawLine(0f,i*(screenHeight/GConst.TOWERGRIDHEIGHT),screenWidth,i*(screenHeight/GConst.TOWERGRIDHEIGHT),paintLines)
        }
    }
}