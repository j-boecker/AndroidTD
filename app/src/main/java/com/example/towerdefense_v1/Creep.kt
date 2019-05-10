package com.example.towerdefense_v1

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix


class Creep(images: Map<String, Bitmap>, x : Float, y : Float, var maxHp: Float, var speed: Float, var path: MutableList<Direction>) : Sprite(images,x,y) {

    var matrix: Matrix = Matrix()
    var timer: Float = 0f
    var direction: Direction
    var pathPointer = 0
    init {
        matrix.setTranslate(x * xPixScale*2f,y * yPixScale*2f)
        matrix.postScale(0.5f,0.5f)
        direction = path.first()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap((images[GConst.CREEP_IMAGE1]) as Bitmap, matrix, null)
    }

    override fun update() {
        move()
    }

    fun move(){

        when(direction){
            Direction.UP -> y-=speed
            Direction.DOWN -> y+=speed
            Direction.LEFT -> x-=speed*0.8f //this is just due to ratio issues displaying it on screen
            Direction.RIGHT -> x+=speed*0.8f
        }

        if(timer >=GConst.CELLSIZE/speed){
            direction = path[pathPointer]
            if(pathPointer<path.size-1){
                pathPointer++
            }
            else{
                pathPointer=0
            }

            timer = 0f
        }
       timer++

        matrix.setTranslate(x * xPixScale*2f,y * yPixScale*2f)
        matrix.postScale(0.5f,0.5f)
    }
}