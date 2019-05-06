import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class Projectile : Sprite {

    protected var xDistance: Int = 0
    protected var yDistance: Int = 0
    protected var distance: Float = 0.toFloat()
    protected var creep: Creep? = null
    protected var damage = 1.0
    protected var speed = 1.0


    protected var img: BufferedImage? = null
    protected var degree = 45.0

    protected var currentSprite: Sprite


    constructor() {}

    constructor(pX: Int, pY: Int) : super(pX, pY) {}


    fun setCreep(pCreep: Creep) {
        creep = pCreep
        xDistance = creep!!.x + creep!!.width / 2 - (this.x + this.width / 2)
        yDistance = creep!!.y + creep!!.height / 2 - (this.y + this.height / 2)
        distance = Math.sqrt(Math.pow(xDistance.toDouble(), 2.0) + Math.pow(yDistance.toDouble(), 2.0)).toFloat()
    }

    fun setDamage(pDamage: Double) {
        damage = pDamage
    }

    fun setSpeed(pSpeed: Double) {
        speed = pSpeed
    }


    protected fun computeDegree() {
        degree = Math.toDegrees(Math.atan2(xDistance.toDouble(), yDistance.toDouble()))
        if (degree < 0) {
            degree += 360.0
        }
    }


    protected fun hitCreep(pCreep: Creep) {
        pCreep.damage(damage)
        this.dispose()
    }


    override fun doLogic(deltaTime: Float) {

        if (creep == null && xDistance == 0 && yDistance == 0) {
            dispose()
            return
        } else if (creep != null && (creep!!.isDisposable == false || creep!!.isDisposable && xDistance == 0 && yDistance == 0)) {
            xDistance = creep!!.x + creep!!.width / 2 - (this.x + this.width / 2)
            yDistance = creep!!.y + creep!!.height / 2 - (this.y + this.height / 2)
            distance = Math.sqrt(Math.pow(xDistance.toDouble(), 2.0) + Math.pow(yDistance.toDouble(), 2.0)).toFloat()
        }

        this.move(
            (xDistance / (distance / speed)).toFloat().toDouble(),
            (yDistance / (distance / speed)).toFloat().toDouble()
        )

        for (i in 0 until Arena.getSpriteList().size()) {
            currentSprite = Arena.getSpriteList()[i]
            if (currentSprite is Creep) {
                if (this.collidesWith(currentSprite)) {
                    hitCreep(currentSprite as Creep)
                    break
                }
            }
        }

        if (x > Arena.getWidth() * Arena.getCellSize() || x < 0 || y > Arena.getHeight() * Arena.getCellSize() || y < 0) {
            this.dispose()
        }

        computeDegree()
    }


    fun draw(g: Graphics) {
        if (img == null) {
            g.setColor(Color.blue)
            g.fillRect(x, y, width, height)
        } else {
            val g2d: Graphics2D
            g2d = g.create() as Graphics2D
            g2d.rotate(Math.toRadians(180 - degree), x + width / 2, y + height / 2)

            g2d.drawImage(img, x, y, 30, 30, null)
            g2d.dispose()
        }
    }

}
