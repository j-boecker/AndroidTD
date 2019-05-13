package com.example.towerdefense_v1

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Log

open class Projectile(images: Map<String, Bitmap>, x: Float, y: Float,var speed: Float, var damage: Float, var creepList: MutableList<Creep>) : Sprite(images, x, y) {

    var xDistance = 0f
    var yDistance = 0f
    var distance: Float = 0f
    lateinit var creep : Creep
    var matrix: Matrix = Matrix()
    var degree = 45f

    init {
        matrix.setTranslate(x * xPixScale*2f,y * yPixScale*2f)
        matrix.postScale(0.5f,0.5f)
        currentBitmap = images[GConst.PROJECTILE_IMAGE1] as Bitmap
        width = 20f
        height = 20f
    }

    fun hitCreep(pCreep: Creep){
        pCreep.takeDamage(damage)
        this.isDisposable = true
    }

    fun setaCreep(pCreep: Creep){
        creep = pCreep
        //calculating the distance with pytagoras
        xDistance = ((creep.x + creep.width/2) - (this.x +this.width /2))
        yDistance = ((creep.y + creep.height/2) - (this.y +this.height /2))
        distance = (Math.sqrt(Math.pow(xDistance.toDouble(),2.0) + Math.pow(yDistance.toDouble(),2.0))).toFloat()

    }

    fun computeDegrees(){
        degree = Math.toDegrees((Math.atan2(xDistance.toDouble(), yDistance.toDouble()))).toFloat()
        if(degree < 0){
            degree += 360
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(currentBitmap, matrix, null)
    }

    override fun update() {
      if(creep == null && xDistance < 1f && yDistance < 1f){
          isDisposable = true
          return
      }
        else if(creep != null){

          xDistance = creep.x - this.x
          yDistance = creep.y - this.y
          distance = (Math.sqrt(Math.pow(xDistance.toDouble(),2.0) + Math.pow(yDistance.toDouble(),2.0))).toFloat()
      }
        //this.move((xDistance / (distance / speed)),(xDistance / (distance / speed)))
        this.x += xDistance/ (distance/speed)
        this.y += yDistance/ (distance/speed)
        computeDegrees()
        matrix.setRotate(degree)
        matrix.setTranslate(x * xPixScale*2f,y * yPixScale*2f)
        matrix.postScale(0.5f,0.5f)



        // checking if it collides with a creep(doesn't have to be the creep to was aimed at)
        for(c in creepList){
            if(c.collidesWith(this)){
                Log.d("gameDebug", "collided")
                hitCreep(c)
                this.isDisposable = true
                break
            }
        }

    }


}