import java.awt.Graphics

class Tower(pCellX: Int, pCellY: Int, cellSize: Int) : Sprite(cellSize * pCellX, cellSize * pCellY) {

    var cellX: Int = 0
        protected set
    var cellY: Int = 0
        protected set
    var gold: Int = 0
    var speed: Double = 0.toDouble()
    var projectileSpeed: Double = 0.toDouble()
    var range: Int = 0
    var name: String? = null
    var damage: Int = 0
    var level: Int = 0
    var upgradeCost: Int = 0

    private var currentSprite: Sprite? = null


    val nextCreep: Creep?
        get() {
            var currentCreep: Creep? = null
            var nextCreep: Creep? = null
            var distance: Double
            var minDistance = 1000.0

            for (i in 0 until Arena.getSpriteList().size()) {

                currentSprite = Arena.getSpriteList()[i]
                if (currentSprite is Creep) {
                    currentCreep = currentSprite as Creep?
                    distance = getDistanceToCreep(currentCreep)
                    if (distance < minDistance) {
                        minDistance = distance
                        nextCreep = currentCreep

                    }
                }
            }
            return nextCreep
        }

    init {

        this.width = cellSize
        this.height = cellSize

        cellX = pCellX
        cellY = pCellY

    }

    //	public boolean isUpgradable(){
    //		return false;
    //	}

    fun upgrade() {}

    fun getDistanceToCreep(currentCreep: Creep?): Double {
        var distance = Integer.MAX_VALUE.toDouble()
        if (currentCreep != null) {
            distance = Math.sqrt(
                Math.pow(
                    this.x + this.width / 2 - currentCreep!!.getX() - currentCreep!!.getWidth() / 2,
                    2.0
                ) + Math.pow(this.y + this.height / 2 - currentCreep!!.getY() - currentCreep!!.getHeight() / 2, 2.0)
            )
        }
        return distance
    }


    fun shoot(creep: Creep, projectile: Projectile) {
        //		ProjectilePeter projectile = new ProjectilePeter(this.getX() + 20, this.getY() + 20, null, getDamage(), 5);
        projectile.setLocation(
            this.x + this.width / 2 - projectile.getWidth() / 2,
            this.y + this.height / 2 - projectile.getHeight() / 2
        )
        projectile.setCreep(creep)
        projectile.setDamage(damage)
        projectile.setSpeed(projectileSpeed)
        Arena.addSprite(projectile)
    }


    fun draw(g: Graphics) {
        g.fillRect(x, y, width, height)
    }

}
