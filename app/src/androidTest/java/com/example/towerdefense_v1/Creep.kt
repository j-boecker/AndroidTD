import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage

class Creep : Sprite {

    var cellX: Int = 0
        private set
    var cellY: Int = 0
        private set

    private var direction = Direction.RIGHT

    private var timer = 0.0
    private var speed: Double = 0.toDouble()
    private var speedMultiplier = 1.0
    private var damage = 3.0

    private var effectTimer = 0.0

    private var stunned = false
    private var stunTimer = 0.0

    private var burnDamage = 0.0
    private var burnTimer = 0.29


    private var movementX: Float = 0.toFloat()
    private var movementY: Float = 0.toFloat()

    private var hp: Double = 0.toDouble()
    private var maxHp: Double = 0.toDouble()

    internal var target: Target? = null
    private var path: MutableList<Direction>? = null

    private val VALUE: Int


    private var imgTimer = 0.0
    private var imageArray: Array<BufferedImage>? = null
    private val imageArrayFrozen: Array<BufferedImage>


    constructor(
        cellSize: Int,
        pHp: Double,
        pSpeed: Double,
        pDamage: Double,
        pValue: Int,
        pImageArray: Array<BufferedImage>,
        pImageArrayFrozen: Array<BufferedImage>
    ) {
        width = cellSize
        height = cellSize

        hp = pHp
        maxHp = pHp

        speed = pSpeed
        damage = pDamage

        VALUE = pValue

        target = Arena.getTarget()

        imageArray = pImageArray
        imageArrayFrozen = pImageArrayFrozen
    }


    constructor(
        pCellX: Int,
        pCellY: Int,
        cellSize: Int,
        pHp: Double,
        pSpeed: Double,
        pDamage: Double,
        pValue: Int,
        pImageArray: Array<BufferedImage>,
        pImageArrayFrozen: Array<BufferedImage>
    ) : super(cellSize * pCellX, cellSize * pCellY) {

        cellX = pCellX
        cellY = pCellY

        width = cellSize
        height = cellSize

        hp = pHp
        maxHp = pHp

        speed = pSpeed
        damage = pDamage

        VALUE = pValue

        target = Arena.getTarget()

        imageArray = pImageArray
    }

    fun setPath(pPath: MutableList<Direction>) {
        path = pPath
    }

    private fun move(deltaTime: Float) {

        imgTimer += deltaTime.toDouble()
        this.move(
            movementX.toDouble() * deltaTime.toDouble() * speed * speedMultiplier,
            movementY.toDouble() * deltaTime.toDouble() * speed * speedMultiplier
        )
        timer += deltaTime * speedMultiplier

        if (timer >= 1 / speed) {
            timer = 0.0

            this.x = cellX * Arena.getCellSize()
            this.y = cellY * Arena.getCellSize()

            movementX = 0f
            movementY = 0f

            if (path != null && !path!!.isEmpty()) {
                changeDirection(path!![0])
                path!!.removeAt(0)
            }

            when (direction) {
                RIGHT -> {
                    if (cellX + 1 >= Arena.getWidth() || !Arena.isFree(cellX + 1, cellY)) {
                        return
                    }
                    cellX++
                    movementX = Arena.getCellSize()
                    movementY = 0f
                }
                DOWN -> {
                    if (cellY + 1 >= Arena.getHeight() || !Arena.isFree(cellX, cellY + 1)) {
                        return
                    }
                    cellY++
                    movementX = 0f
                    movementY = Arena.getCellSize()
                }
                LEFT -> {
                    if (cellX <= 0 || !Arena.isFree(cellX - 1, cellY)) {
                        return
                    }
                    cellX--
                    movementX = -Arena.getCellSize()
                    movementY = 0f
                }
                UP -> {
                    if (cellY <= 0 || !Arena.isFree(cellX, cellY - 1)) {
                        return
                    }
                    cellY--
                    movementX = 0f
                    movementY = -Arena.getCellSize()
                }
            }
        }
    }

    fun setCellLocation(pX: Int, pY: Int) {
        cellX = pX
        cellY = pY
        super.setLocation(Arena.getCellSize() * pX, Arena.getCellSize() * pY)
    }


    fun changeDirection(dir: Direction) {
        direction = dir
    }


    fun damage(pDamage: Double) {
        hp -= pDamage
    }


    fun slowDown(multiplier: Double, duration: Double) {
        speedMultiplier = multiplier
        effectTimer = duration
    }

    fun stun(duration: Double) {
        stunned = true
        stunTimer = duration
    }


    override fun doLogic(deltaTime: Float) {
        effectTimer -= deltaTime.toDouble()
        if (effectTimer <= 0) speedMultiplier = 1.0
        stunTimer -= deltaTime.toDouble()
        if (stunTimer <= 0) stunned = false


        if (burnDamage > 0) {

            burnTimer += deltaTime.toDouble()
            if (burnTimer > 0.3) {
                this.damage(burnDamage)
                burnTimer = 0.0
                burnDamage = 0.0
            }
        }

        if (!stunned) move(deltaTime)

        if (hp <= 0) {
            this.dispose()
        }

        if (collidesWith(Arena.getTarget())) {
            target!!.takeHit(damage)
            dispose()
        }

    }

    fun burn(pDamage: Double) {
        burnDamage = pDamage

    }

    fun draw(g: Graphics) {

        when (direction) {
            RIGHT -> {
                g.drawImage(imageArray!![3], x + 10, y, 50, 50, null)

                if (imgTimer < 0.33) {
                    if (speedMultiplier >= 1) {
                        g.drawImage(imageArray!![0], x, y, 50, 50, null)
                    } else {
                        g.drawImage(imageArrayFrozen[0], x, y, 50, 50, null)
                    }

                } else if (imgTimer < 0.66) {
                    if (speedMultiplier >= 1) {
                        g.drawImage(imageArray!![1], x, y, 50, 50, null)
                    } else {
                        g.drawImage(imageArrayFrozen[1], x, y, 50, 50, null)
                    }

                } else if (imgTimer < 1) {
                    if (speedMultiplier >= 1) {
                        g.drawImage(imageArray!![2], x, y, 50, 50, null)
                    } else {
                        g.drawImage(imageArrayFrozen[2], x, y, 50, 50, null)
                    }

                } else if (imgTimer < 1.33) {
                    if (speedMultiplier >= 1) {
                        g.drawImage(imageArray!![1], x, y, 50, 50, null)
                    } else {
                        g.drawImage(imageArrayFrozen[1], x, y, 50, 50, null)
                    }

                } else {
                    if (speedMultiplier >= 1) {
                        g.drawImage(imageArray!![1], x, y, 50, 50, null)
                    } else {
                        g.drawImage(imageArrayFrozen[1], x, y, 50, 50, null)
                    }
                    imgTimer = 0.0
                }
            }
            DOWN -> if (imgTimer < 0.5) {
                if (speedMultiplier >= 1) {
                    g.drawImage(imageArray!![4], x, y, 50, 50, null)
                } else {
                    g.drawImage(imageArrayFrozen[4], x, y, 50, 50, null)
                }

            } else if (imgTimer < 1) {
                if (speedMultiplier >= 1) {
                    g.drawImage(imageArray!![5], x, y, 50, 50, null)
                } else {
                    g.drawImage(imageArrayFrozen[5], x, y, 50, 50, null)
                }

            } else {
                if (speedMultiplier >= 1) {
                    g.drawImage(imageArray!![5], x, y, 50, 50, null)
                } else {
                    g.drawImage(imageArrayFrozen[5], x, y, 50, 50, null)
                }

                imgTimer = 0.0
            }
            LEFT -> if (imgTimer < 0.5) {
                if (speedMultiplier >= 1) {
                    g.drawImage(imageArray!![6], x, y, 50, 50, null)
                } else {
                    g.drawImage(imageArrayFrozen[6], x, y, 50, 50, null)
                }

            } else if (imgTimer < 1) {
                if (speedMultiplier >= 1) {
                    g.drawImage(imageArray!![7], x, y, 50, 50, null)
                } else {
                    g.drawImage(imageArrayFrozen[7], x, y, 50, 50, null)
                }

            } else {
                if (speedMultiplier >= 1) {
                    g.drawImage(imageArray!![7], x, y, 50, 50, null)
                } else {
                    g.drawImage(imageArrayFrozen[7], x, y, 50, 50, null)
                }

                imgTimer = 0.0
            }
            UP -> if (imgTimer < 0.5) {
                if (speedMultiplier >= 1) {
                    g.drawImage(imageArray!![9], x, y, 50, 50, null)
                } else {
                    g.drawImage(imageArrayFrozen[9], x, y, 50, 50, null)
                }

            } else if (imgTimer < 1) {
                if (speedMultiplier >= 1) {
                    g.drawImage(imageArray!![10], x, y, 50, 50, null)
                } else {
                    g.drawImage(imageArrayFrozen[10], x, y, 50, 50, null)
                }

            } else {
                if (speedMultiplier >= 1) {
                    g.drawImage(imageArray!![10], x, y, 50, 50, null)
                } else {
                    g.drawImage(imageArrayFrozen[10], x, y, 50, 50, null)
                }

                imgTimer = 0.0
            }
            else -> {
                g.setColor(Color.red)
                g.fillRect(x, y, width, height)
            }
        }

        g.setColor(Color.RED)
        g.fillRect(x, y, width, 5)
        g.setColor(Color.green)
        if (hp > 0) {
            g.fillRect(x, y, (hp / maxHp * width).toInt(), 5)
        }
    }

    override fun dispose() {
        if (!this.disposable) {
            super.dispose()

            if (hp <= 0) {
                Arena.addGold(VALUE)
            }
        }
    }


}
