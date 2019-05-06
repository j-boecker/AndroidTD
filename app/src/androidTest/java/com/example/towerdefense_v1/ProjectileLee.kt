import java.io.File
import java.io.IOException

import javax.imageio.ImageIO

class ProjectileLee(pSpeed: Double, pDamage: Double) : Projectile() {

    private var disposeTimer = 0.0

    init {
        this.width = 30
        this.height = 30
        this.setSpeed(pSpeed)
        this.setDamage(pDamage)

        try {
            img = ImageIO.read(File("img/Lee/Lee_Projectile2.png"))
        } catch (e: IOException) {
            // e.printStackTrace();
            println("cant read input file")
        }

    }

    override fun doLogic(deltaTime: Float) {
        disposeTimer += deltaTime.toDouble()
        if (disposeTimer > 1.4) {
            this.dispose()
        }

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

        if (x > Arena.getWidth() * Arena.getCellSize() || x < 0 || y > Arena.getHeight() * Arena.getCellSize()
            || y < 0
        ) {
            this.dispose()
        }

        computeDegree()
    }

}