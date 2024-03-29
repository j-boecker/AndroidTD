package com.example.towerdefense_v1

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Binder
import android.os.IBinder
import android.view.SurfaceHolder
import java.lang.Exception

class GameThread() : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return GameServiceBinder()
    }

    lateinit var surfaceHolder: SurfaceHolder
    lateinit var gameView: GView

    private var running: Boolean = false
    private val targetFPS = 50
    lateinit var canvas: Canvas
    val innerThread = InnerThread()
    var surfaceActive = false

    //class to outsource loading images from drawable
    var imgLoader: ImageLoader

    var spriteList: MutableList<Sprite>
    var creepList: MutableList<Creep>
    var towerBuildGrid: Array<Array<Tower?>>
    var garbageSpriteIndex = -1

    var imagesArcher: MutableMap<String, Bitmap> = mutableMapOf()
    var imagesCreep: MutableMap<String, Bitmap> = mutableMapOf()


    init {

        imgLoader = ImageLoader()
        imagesArcher = imgLoader.imagesArcher
        imagesCreep = imgLoader.imagesCreep

        spriteList = mutableListOf<Sprite>()
        creepList = mutableListOf<Creep>()
        //making a 2d array tile grid in kotlin is terrible
        towerBuildGrid = Array(GConst.TOWERGRIDWIDTH) { Array<Tower?>(GConst.TOWERGRIDHEIGHT) { null } }

        createTower(0, 3, "Archer")

        createTower(7, 9, "Archer")

        createCreep()
    }

    fun setRunning(running: Boolean) {
        this.running = running
    }

    fun getRunning(): Boolean {
        return this.running
    }

    inner class GameServiceBinder : Binder() {
        fun getService(): GameThread {
            return this@GameThread
        }
    }

    inner class InnerThread : Thread() {
        override fun run() {
            var startTime: Long
            var timeMillis: Long
            var waitTime: Long
            val targetTime = (1000 / targetFPS).toLong()

            while (running) {
                if (surfaceActive) {
                    startTime = System.nanoTime()
                    try {
                        canvas = surfaceHolder.lockCanvas()

                        synchronized(surfaceHolder) {
                            gameView.draw(canvas)
                            draw(canvas)
                            update()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    timeMillis = (System.nanoTime() - startTime) / 1000000
                    waitTime = targetTime - timeMillis

                    try {
                        sleep(Math.max(0, waitTime))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    startTime = System.nanoTime()
                    update()
                    timeMillis = (System.nanoTime() - startTime) / 1000000
                    waitTime = targetTime - timeMillis

                    try {
                        sleep(waitTime)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun update() {

        for (tList in towerBuildGrid) {
            for (tw in tList) {
                tw?.update()
            }
        }
        for (s in spriteList) {
            if (s.isDisposable) {
                garbageSpriteIndex = spriteList.indexOf(s)
            } else {
                s.update()
            }
        }
        if (garbageSpriteIndex != -1) {
            spriteList.removeAt(garbageSpriteIndex)
            garbageSpriteIndex = -1
        }
        for (c in creepList) {

            if (c.isDisposable) {
                garbageSpriteIndex = creepList.indexOf(c)
            } else {
                c.update()
            }
        }

        if (garbageSpriteIndex != -1) {
            creepList.removeAt(garbageSpriteIndex)
            garbageSpriteIndex = -1
        }
        creepLoop()
    }

    fun draw(canvas: Canvas) {
        if (surfaceActive) {
            for (tList in towerBuildGrid) {
                for (tw in tList) {
                    tw?.draw(canvas)
                }
            }
            for (s in spriteList) {
                s.draw(canvas)
            }
            for (c in creepList) {
                c.draw(canvas)
            }
        }
    }

    fun createTower(xGrid: Int, yGrid: Int, towerName: String) {
        when (towerName) {
            "Archer" -> {
                towerBuildGrid[xGrid][yGrid] =
                    TowerArcher(imagesArcher, spriteList, creepList, xGrid * 67f, yGrid * GConst.CELLSIZE.toFloat())
            }
        }
    }

    var creepTimer = 0f
    fun creepLoop() {
        if (creepTimer > 100) {
            createCreep()
            creepTimer = 0f
        }
        creepTimer++
    }

    fun createCreep() {
        var path = mutableListOf(
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.DOWN,
            Direction.DOWN,
            Direction.DOWN,
            Direction.LEFT,
            Direction.LEFT,
            Direction.UP,
            Direction.UP
        )
        var creep = Creep(imagesCreep, 50f, 50f, 30f, 2f, path)
        creepList.add(creep)
    }
}