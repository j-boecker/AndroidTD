package com.example.towerdefense_v1

import android.graphics.*


class Creep(images: Map<String, Bitmap>, x : Float, y : Float, var hitPoints: Float, var speed: Float, var path: MutableList<Direction>) : Sprite(images,x,y) {

    var matrix: Matrix = Matrix()
    var timer: Float = 0f
    var direction: Direction
    var pathPointer = 0
    var lifebarPaint: Paint
    init {
        lifebarPaint = Paint()
        lifebarPaint.setColor(Color.GREEN)
        lifebarPaint.setStyle(Paint.Style.FILL)
        matrix.setTranslate(x * xPixScale*2f,y * yPixScale*2f)
        matrix.postScale(0.5f,0.5f)
        direction = path.first()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(x*xPixScale, y*yPixScale+10f, x*xPixScale+hitPoints*3, y*yPixScale-5, lifebarPaint )
        canvas.drawBitmap((images[GConst.CREEP_IMAGE1]) as Bitmap, matrix, null)
    }

    override fun update() {
        move()
        if(hitPoints<=0){
            this.isDisposable = true
        }
    }
    fun takeDamage(damage: Float){
        hitPoints -= damage
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