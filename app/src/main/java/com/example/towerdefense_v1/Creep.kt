package com.example.towerdefense_v1

import android.graphics.*


class Creep(
    images: Map<String, Bitmap>,
    x: Float,
    y: Float,
    var hitPoints: Float,
    var speed: Float,
    var path: MutableList<Direction>
) : Sprite(images, x, y) {

    var matrix: Matrix = Matrix()
    var timer: Float = 0f
    var direction: Direction
    var pathPointer = 0
    var lifebarPaint: Paint
    var animTimer = 0
    lateinit var altBitmap: Bitmap
    lateinit var altBitmap2: Bitmap

    init {
        lifebarPaint = Paint()
        lifebarPaint.setColor(Color.GREEN)
        lifebarPaint.setStyle(Paint.Style.FILL)
        matrix.setTranslate(x * xPixScale * 2f, y * yPixScale * 2f)
        matrix.postScale(0.5f, 0.5f)
        direction = path.first()
        altBitmap = (images[GConst.CREEP_IMAGE1]) as Bitmap
        altBitmap2 = (images[GConst.CREEP_IMAGE2]) as Bitmap
        currentBitmap = altBitmap
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(
            x * xPixScale,
            y * yPixScale + 10f,
            x * xPixScale + hitPoints * 3,
            y * yPixScale - 5,
            lifebarPaint
        )
        canvas.drawBitmap(currentBitmap, matrix, null)
    }

    override fun update() {
        move()
        if (hitPoints <= 0) {
            this.isDisposable = true
        }
        when (animTimer) {
            in 0..15 -> currentBitmap = altBitmap2
            in 16..30 -> currentBitmap = altBitmap
            31 -> {
                currentBitmap = altBitmap
                animTimer = 0
            }
        }
        animTimer++
    }

    fun takeDamage(damage: Float) {
        hitPoints -= damage
    }

    fun move() {

        when (direction) {
            Direction.UP -> {
                y -= speed
                altBitmap2 = (images[GConst.CREEP_IMAGE7]) as Bitmap
                altBitmap = (images[GConst.CREEP_IMAGE8]) as Bitmap
            }
            Direction.DOWN -> {
                y += speed
                altBitmap2 = (images[GConst.CREEP_IMAGE5]) as Bitmap
                altBitmap = (images[GConst.CREEP_IMAGE6]) as Bitmap
            }
            Direction.LEFT -> {
                x -= speed * 0.8f
                altBitmap2 = (images[GConst.CREEP_IMAGE3]) as Bitmap
                altBitmap = (images[GConst.CREEP_IMAGE4]) as Bitmap
            } //this is just due to ratio issues displaying it on screen
            Direction.RIGHT -> {
                x += speed * 0.8f
                altBitmap2 = (images[GConst.CREEP_IMAGE1]) as Bitmap
                altBitmap = (images[GConst.CREEP_IMAGE2]) as Bitmap
            }
        }

        if (timer >= GConst.CELLSIZE / speed) {
            direction = path[pathPointer]
            if (pathPointer < path.size - 1) {
                pathPointer++
            } else {
                pathPointer = 0
            }

            timer = 0f
        }
        timer++

        matrix.setTranslate(x * xPixScale * 2f, y * yPixScale * 2f)
        matrix.postScale(0.5f, 0.5f)

    }
}