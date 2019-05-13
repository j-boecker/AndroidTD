package com.example.towerdefense_v1

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Resources
import android.graphics.*
import android.os.IBinder
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast

class GView(ctx: Context) : SurfaceView(ctx), SurfaceHolder.Callback {

    var serviceStarted = false
    val backPaint = Paint()
    val imageGrass: Bitmap
    val paintBig = Paint()
    val paintLines = Paint()
    var screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
    var screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
    lateinit var gameThread: GameThread

    val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            this@GView.gameThread = ((binder as GameThread.GameServiceBinder).getService())
            this@GView.gameThread.gameView = this@GView
            this@GView.gameThread.surfaceHolder = this@GView.holder
            if (!this@GView.gameThread.getRunning()) {
                this@GView.gameThread.setRunning(true)
                this@GView.gameThread.innerThread.start()
            }
            this@GView.gameThread.surfaceActive = true
            Toast.makeText(context, "service connected", Toast.LENGTH_SHORT)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Toast.makeText(context, "service disconnected", Toast.LENGTH_LONG)
        }
    }


    init {

        paintLines.color = Color.LTGRAY
        //paintLines.alpha = 100
        paintBig.isAntiAlias = true
        paintBig.isFilterBitmap = true
        paintBig.isDither = true
        backPaint.setColor(Color.RED)
        // putting the images in a map containing all bitmaps. Here a String Constant resource could be used to avoid spelling mistakes
        imageGrass = BitmapFactory.decodeResource(ctx.resources, R.drawable.grass)
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {

        if (!serviceStarted) {
            val startIntent = Intent(context, GameThread::class.java)
            context.startService(startIntent)
            serviceStarted = true
            //bind service

        }
        val bindIntent = Intent(context, GameThread::class.java)
        context.bindService(bindIntent, serviceConn, Context.BIND_AUTO_CREATE)

        this.setOnTouchListener { view, motionEvent ->
            var xGrid = (motionEvent.x / screenWidth) * GConst.TOWERGRIDWIDTH
            var yGrid = (motionEvent.y / screenWidth) * GConst.TOWERGRIDWIDTH
            this@GView.gameThread.createTower(xGrid.toInt(), yGrid.toInt(), "Archer")
            true
        }

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        this@GView.gameThread.surfaceActive = false
        context.unbindService(serviceConn)
    }


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
        screenHeight = Resources.getSystem().displayMetrics.heightPixels.toFloat()
        if (screenHeight < screenWidth)
            screenWidth = screenWidth * 0.8f
        else
            screenHeight = screenHeight * 0.8f

        canvas?.drawBitmap(imageGrass, 0f, 0f, paintBig)
        for (i in 1..GConst.TOWERGRIDWIDTH) {
            canvas?.drawLine(
                i * (screenWidth / GConst.TOWERGRIDWIDTH),
                0f,
                i * (screenWidth / GConst.TOWERGRIDWIDTH),
                screenHeight,
                paintLines
            )
        }
        for (i in 1..GConst.TOWERGRIDHEIGHT) {
            canvas?.drawLine(
                0f,
                i * (screenHeight / GConst.TOWERGRIDHEIGHT),
                screenWidth,
                i * (screenHeight / GConst.TOWERGRIDHEIGHT),
                paintLines
            )
        }
    }
}