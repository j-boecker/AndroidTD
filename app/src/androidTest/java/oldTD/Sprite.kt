import java.awt.Graphics

abstract class Sprite {

    var x: Int = 0
        protected set
    var y: Int = 0
        protected set

    protected var xFloat: Float = 0.toFloat()
    protected var yFloat: Float = 0.toFloat()

    var width: Int = 0
        protected set
    var height: Int = 0
        protected set

    var isDisposable = false
        protected set


    constructor() {
        x = 0
        y = 0
    }

    constructor(pX: Int, pY: Int) {
        x = pX
        y = pY
    }


    fun doLogic(deltaTime: Float) {}

    fun draw(g: Graphics) {}


    fun setSize(pWidth: Int, pHeight: Int) {
        width = pWidth
        height = pHeight
    }

    fun setLocation(pX: Int, pY: Int) {
        x = pX
        y = pY
    }

    fun move(distanceX: Double, distanceY: Double) {
        this.x += distanceX.toInt()
        var temp = (distanceX * 100).toInt()
        temp -= distanceX.toInt() * 100
        xFloat += temp.toFloat()
        if (xFloat >= 100) {
            xFloat -= 100f
            this.x += 1
        } else if (xFloat <= -1 * 100) {
            xFloat += 100f
            this.x -= 1
        }

        this.y += distanceY.toInt()
        temp = (distanceY * 100).toInt()
        temp -= distanceY.toInt() * 100
        yFloat += temp.toFloat()
        if (yFloat >= 100) {
            yFloat -= 100f
            this.y += 1
        } else if (yFloat <= -1 * 100) {
            yFloat += 100f
            this.y -= 1
        }
    }

    fun dispose() {
        isDisposable = true
    }


    fun collidesWith(sprite: Sprite): Boolean {
        return if (x <= sprite.x + sprite.width && x + width >= sprite.x && y <= sprite.y + sprite.height && y + height >= sprite.y)
            true
        else
            false
    }

}
