import java.util.ArrayList

class Spawner(cellX: Int, cellY: Int, private val frequency: Float) : Sprite(cellX, cellY) {

    private var timer = 0f

    private val pathValues: Array<IntArray>
    private val path: List<Direction>?

    private var creepList: MutableList<Creep>? = null


    init {

        pathValues = Array(Arena.getWidth()) { IntArray(Arena.getHeight()) }
        //		int maxValue = Arena.getWidth()*Arena.getHeight();
        //		for(int x = 0; x < pathValues.length; x++){
        //			for(int y = 0; y < pathValues[0].length; y++){
        //				pathValues[x][y] = maxValue;
        //			}
        //		}

        path = findPath()
    }


    override fun doLogic(deltaTime: Float) {
        timer += deltaTime
        if (creepList == null || creepList!!.isEmpty()) {
            dispose()
        } else if (timer >= frequency) {
            timer -= frequency
            //			Creep creep = new Creep(this.x, this.y, Arena.getCellSize());
            val creep = creepList!![0]
            creepList!!.removeAt(0)
            val pathCopy = ArrayList<Direction>()
            if (path != null) pathCopy.addAll(path)
            creep.setCellLocation(x, y)
            creep.setPath(pathCopy)
            Arena.addSprite(creep)
        }
    }


    fun setCreepList(pCreepList: MutableList<Creep>) {
        creepList = pCreepList
    }


    fun findPath(): List<Direction>? {

        val maxValue = Arena.getWidth() * Arena.getHeight()
        for (x in pathValues.indices) {
            for (y in 0 until pathValues[0].size) {
                pathValues[x][y] = maxValue
            }
        }

        return findPath(x, y, 0)
    }

    private fun findPath(startCellX: Int, startCellY: Int, length: Int): MutableList<Direction>? {
        var directions: MutableList<Direction>?
        val target = Arena.getTarget()
        if (target.getCellX() === startCellX && target.getCellY() === startCellY) {
            directions = ArrayList<Direction>()
        } else if (startCellX < 0 || startCellX >= Arena.getWidth() || startCellY < 0 || startCellY >= Arena.getHeight() || length >= pathValues[startCellX][startCellY] || !Arena.isFree(
                startCellX,
                startCellY
            )
        ) {
            directions = null
        } else {
            pathValues[startCellX][startCellY] = length
            var temp: MutableList<Direction>?
            directions = findPath(startCellX + 1, startCellY, length + 1)    //RIGHT
            directions?.add(0, Direction.RIGHT)

            temp = findPath(startCellX - 1, startCellY, length + 1)        //LEFT
            temp?.add(0, Direction.LEFT)
            if (temp != null && (directions == null || temp.size < directions.size)) {
                directions = temp
            }

            temp = findPath(startCellX, startCellY - 1, length + 1)        //UP
            temp?.add(0, Direction.UP)
            if (temp != null && (directions == null || temp.size < directions.size)) {
                directions = temp
            }

            temp = findPath(startCellX, startCellY + 1, length + 1)        //DOWN
            temp?.add(0, Direction.DOWN)
            if (temp != null && (directions == null || temp.size < directions.size)) {
                directions = temp
            }
        }
        return directions
    }

}
