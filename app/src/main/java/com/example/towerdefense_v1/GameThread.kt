package com.example.towerdefense_v1
import android.graphics.Canvas
import android.view.SurfaceHolder
import java.lang.Exception

class GameThread(val surfaceHolder : SurfaceHolder, val gameView : GView) : Thread() {

    private var running : Boolean = false
    private val targetFPS = 50
    lateinit var canvas: Canvas

    fun setRunning(running: Boolean){
        this.running = running
    }

    override fun run() {
        var startTime : Long
        var timeMillis : Long
        var waitTime : Long
        val targetTime = ( 1000 / targetFPS).toLong()

        while(running){
            startTime = System.nanoTime()

            try {
                canvas = this.surfaceHolder.lockCanvas()

                synchronized(surfaceHolder){
                    this.gameView.update()
                    this.gameView.draw(canvas)
                }
            }
            catch (e : Exception){
                e.printStackTrace()
            }
            finally{
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    }
                    catch( e : Exception){
                        e.printStackTrace()
                    }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            try{
                sleep(waitTime)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}