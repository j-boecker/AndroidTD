package com.example.towerdefense_v1
import android.app.Service
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.SurfaceHolder
import java.lang.Exception

class GameThread(val surfaceHolder : SurfaceHolder, val gameView : GView){


    private var running : Boolean = false
    private val targetFPS = 50
    lateinit var canvas: Canvas
    val innerThread = InnerThread()

    var spriteList: MutableList<Sprite>
    var towerBuildGrid: Array<Array<Tower?>>


    var imagesArcher: MutableMap<String, Bitmap> = mutableMapOf()
    var imagesCreep: MutableMap<String, Bitmap> = mutableMapOf()


    init{
        imagesArcher = gameView.imagesArcher
        imagesCreep = gameView.imagesCreep

        spriteList = mutableListOf<Sprite>()
        //making a 2d array tile grid in kotlin is terrible
        towerBuildGrid = Array(GConst.TOWERGRIDWIDTH) { Array<Tower?>(GConst.TOWERGRIDHEIGHT) { null } }

        createTower(0,0,"Archer")
        createTower(0,3,"Archer")
        createTower(0,4,"Archer")
        createTower(0,7,"Archer")
        createTower(0,8,"Archer")
        createTower(0,9,"Archer")
        createTower(1,9,"Archer")
        createTower(2,9,"Archer")
        createTower(3,9,"Archer")
        createTower(4,9,"Archer")
        createTower(5,9,"Archer")
        createTower(6,9,"Archer")
        createTower(7,9,"Archer")

        createCreep()


    }

    fun setRunning(running: Boolean){
        this.running = running
    }

    inner class InnerThread : Thread(){
        override fun run() {
            var startTime : Long
            var timeMillis : Long
            var waitTime : Long
            val targetTime = ( 1000 / targetFPS).toLong()

            while(running){
                startTime = System.nanoTime()

                try {
                    canvas = surfaceHolder.lockCanvas()

                    synchronized(surfaceHolder){
                        gameView.draw(canvas)
                        draw(canvas)
                        update()
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

    fun update(){
        for (tList in towerBuildGrid) {
            for (tw in tList) {
                tw?.update()
            }
        }
        for(s in spriteList){
            s.update()
        }
        creepLoop()
    }

   fun draw(canvas: Canvas) {

        for (tList in towerBuildGrid) {
            for (tw in tList) {
                tw?.draw(canvas)
            }
        }
       for(s in spriteList){
           s.draw(canvas)
       }
    }

    fun createTower(xGrid: Int,yGrid: Int, towerName: String){
        when(towerName){
            "Archer" ->{towerBuildGrid[xGrid][yGrid] = TowerArcher(imagesArcher, xGrid*67f, yGrid*GConst.CELLSIZE.toFloat()) }
        }
    }
    var  creepTimer = 0f
    fun creepLoop(){
        if (creepTimer>100){
            createCreep()
            creepTimer = 0f
        }
        creepTimer++
    }
    fun createCreep(){
        var path = mutableListOf(Direction.RIGHT,Direction.RIGHT,Direction.RIGHT,Direction.RIGHT,Direction.DOWN,Direction.DOWN,Direction.DOWN,Direction.LEFT,Direction.LEFT,Direction.UP,Direction.UP)
        var creep = Creep(imagesCreep, 50f, 50f, 10f, 2f,path)
        spriteList.add(creep)
    }
}