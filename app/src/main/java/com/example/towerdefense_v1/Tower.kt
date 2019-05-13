package com.example.towerdefense_v1

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log

open class Tower(
    images: Map<String, Bitmap>,
    var spriteList: MutableList<Sprite>,
    var creepList: MutableList<Creep>,
    x: Float,
    y: Float,
    var shootSpeed: Float,
    var shootRange: Float
) : Sprite(images, x, y) {

    var shootTriggerTimer = 0f
    var cooldown = false
    var lockedCreep: Creep? = null

    override fun draw(canvas: Canvas) {

    }

    override fun update() {

        if (shootTriggerTimer > shootSpeed) {
            cooldown = false
        }
        if (lockedCreep == null) {

            if (getDistanceToCreep(getClosestCreep()) < shootRange) {
                lockedCreep = getClosestCreep()
            }
        } else {
            // if the creep runs out of range
            if (getDistanceToCreep(lockedCreep) > shootRange) {
                lockedCreep = getClosestCreep()
            }
            // shoots only of the tower is not on cooldown
            else if (!cooldown) {
                if (lockedCreep != null) {

                    this.shoot(lockedCreep!!)
                    shootTriggerTimer = 0f
                    cooldown = true
                    Log.d("gamedebug", "shoot at " + lockedCreep!!.x)
                }
            }
        }
        shootTriggerTimer++
    }


    fun shoot(targetCreep: Creep) {
        val projectile = Projectile(images, this.x + (this.width / 2), this.y + (this.height / 2), 5f, 5f, creepList)
        projectile.creep = targetCreep
        spriteList.add(projectile)
    }

    fun getDistanceToCreep(currentCreep: Creep?): Double {
        var distance = Integer.MAX_VALUE.toDouble()
        if (currentCreep != null) {
            distance = Math.sqrt(
                Math.pow(
                    ((this.x + this.width / 2) - (currentCreep!!.x - currentCreep!!.width / 2)).toDouble(),
                    2.0
                ) + Math.pow(
                    ((this.y + this.height / 2) - (currentCreep!!.y - currentCreep!!.height / 2)).toDouble(),
                    2.0
                )
            )
        }
        return distance
    }

    fun getClosestCreep(): Creep? {
        var closestCreep: Creep? = null
        var distance: Double
        var shortestDistance = Double.MAX_VALUE
        for (c in creepList) {
            distance = getDistanceToCreep(c)
            if (distance < shortestDistance) {
                closestCreep = c
                shortestDistance = distance
            }
        }
        return closestCreep
    }
}