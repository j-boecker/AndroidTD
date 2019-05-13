package com.example.towerdefense_v1

import android.graphics.Bitmap
import android.graphics.Canvas

open class Projectile(images: Map<String, Bitmap>, x: Float, y: Float, var creepList: MutableList<Creep>) : Sprite(images, x, y) {

    var xDistance =  0
    var yDistance = 0
    var distance: Float = 0f
    lateinit var creep : Creep
    var damage = 1f
    var speed = 1f

    lateinit var img: Bitmap
    var degree = 45.0

    lateinit var currentSprite: Sprite

    fun hitCreep(pCreep: Creep){
        pCreep.takeDamage(damage)
        this.isDisposable = true
    }

    fun setaCreep(pCreep: Creep){
        creep = pCreep
        //calculating the distance with pytagoras
        xDistance = ((creep.x + creep.width/2) - (this.x +this.width /2)).toInt()
        yDistance = ((creep.y + creep.height/2) - (this.y +this.height /2)).toInt()
        distance = (Math.sqrt(Math.pow(xDistance.toDouble(),2.0) + Math.pow(yDistance.toDouble(),2.0))).toFloat()

    }

    override fun draw(canvas: Canvas) {

    }

    override fun update() {
      if(creep == null && xDistance == 0 && yDistance == 0){
          isDisposable = true
          return
      }
        else if(creep != null && (!creep.isDisposable || creep.isDisposable && xDistance == 0 && yDistance == 0)){
          xDistance = ((creep.x + creep.width/2) - (this.x +this.width /2)).toInt()
          yDistance = ((creep.y + creep.height/2) - (this.y +this.height /2)).toInt()
          distance = (Math.sqrt(Math.pow(xDistance.toDouble(),2.0) + Math.pow(yDistance.toDouble(),2.0))).toFloat()
      }
        this.move((xDistance / (distance / speed)),(xDistance / (distance / speed)))

        // checking if it collides with a creep(doesn't have to be the creep to was aimed at)
        for(c in creepList){
            if(c.collidesWith(this)){
                hitCreep(c)
                break
            }
        }

    }


}