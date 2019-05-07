import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.image.WritableRaster
import java.io.File
import java.io.IOException
import java.util.ArrayList

import javax.imageio.ImageIO

class Arena(width: Int, height: Int, pCellSize: Int) {

    internal var img: BufferedImage? = null

    init {
        towers = Array<Array<Tower>>(width) { arrayOfNulls<Tower>(height) }
        spriteList = ArrayList<Sprite>()

        cellSize = pCellSize


        initialize()

        try {
            img = ImageIO.read(File("img/grass.jpg"))
            imageArray[0] = ImageIO.read(File("img/Minion/minionRight1.png"))
            imageArray[1] = ImageIO.read(File("img/Minion/minionRight2.png"))
            imageArray[2] = ImageIO.read(File("img/Minion/minionRight3.png"))
            imageArray[3] = ImageIO.read(File("img/Minion/shadowMinionRight.png"))
            imageArray[4] = ImageIO.read(File("img/Minion/minionDown1.png"))
            imageArray[5] = ImageIO.read(File("img/Minion/minionDown2.png"))
            imageArray[6] = ImageIO.read(File("img/Minion/minionLeft1.png"))
            imageArray[7] = ImageIO.read(File("img/Minion/minionLeft2.png"))
            imageArray[8] = ImageIO.read(File("img/Minion/minionLeft3.png"))
            imageArray[9] = ImageIO.read(File("img/Minion/minionUp1.png"))
            imageArray[10] = ImageIO.read(File("img/Minion/minionUp2.png"))
            imageArray[11] = ImageIO.read(File("img/Minion/minionUp3.png"))

            imageArrayFrozen[0] = ImageIO.read(File("img/Minion/minionRight1.png"))
            imageArrayFrozen[1] = ImageIO.read(File("img/Minion/minionRight2.png"))
            imageArrayFrozen[2] = ImageIO.read(File("img/Minion/minionRight3.png"))
            imageArrayFrozen[3] = ImageIO.read(File("img/Minion/shadowMinionRight.png"))
            imageArrayFrozen[4] = ImageIO.read(File("img/Minion/minionDown1.png"))
            imageArrayFrozen[5] = ImageIO.read(File("img/Minion/minionDown2.png"))
            imageArrayFrozen[6] = ImageIO.read(File("img/Minion/minionLeft1.png"))
            imageArrayFrozen[7] = ImageIO.read(File("img/Minion/minionLeft2.png"))
            imageArrayFrozen[8] = ImageIO.read(File("img/Minion/minionLeft3.png"))
            imageArrayFrozen[9] = ImageIO.read(File("img/Minion/minionUp1.png"))
            imageArrayFrozen[10] = ImageIO.read(File("img/Minion/minionUp2.png"))
            imageArrayFrozen[11] = ImageIO.read(File("img/Minion/minionUp3.png"))
        } catch (e: IOException) {
            // e.printStackTrace();
            println("cant read input file")
        }

        filterFrozen()
    }

    private fun filterFrozen() {
        var imgWidth: Int
        var imgHeight: Int
        var imgData: Array<Array<IntArray>>

        for (i in imageArray.indices) {
            imgWidth = imageArrayFrozen[i].getWidth()
            imgHeight = imageArrayFrozen[i].getHeight()
            imgData = Array(imgHeight) { Array(imgWidth) { IntArray(4) } }
            for (h in 0 until imgHeight)
                for (w in 0 until imgWidth)
                    imageArrayFrozen[i].getRaster().getPixel(w, h, imgData[h][w])

            for (x in imgData.indices) {
                for (y in 0 until imgData[0].size) {
                    imgData[x][y][0] = 0
                    imgData[x][y][1] = 0
                }
            }

            val raster = imageArrayFrozen[i].getRaster()
            for (h in 0 until imgHeight)
                for (w in 0 until imgWidth)
                    raster.setPixel(w, h, imgData[h][w])

        }
    }


    fun doLogic(deltaTime: Float) {

        for (x in towers.indices) {
            for (y in 0 until towers[x].size) {
                val tower = towers[x][y]
                if (tower != null) tower!!.doLogic(deltaTime)
            }
        }

        var i = 0
        while (i < spriteList.size) {
            val sprite = spriteList[i]

            if (sprite.isDisposable()) {
                spriteList.removeAt(i)
            } else {
                sprite.doLogic(deltaTime)
                i++
            }
        }

        if (waveFinished() == true) {
            if (buildphase == false) {
                wave++
                addTower(spawnPlace!!)
            }
            buildphase = true
        }
    }

    fun draw(g: Graphics) {
        g.drawImage(img, 0, 0, towers.size * cellSize, towers[0].size * cellSize, null)

        //Drawing the arena:
        if (buildphase) {
            g.setColor(Color.gray)
            for (x in towers.indices) {
                g.drawLine(x * cellSize, 0, x * cellSize, towers[0].size * cellSize)
            }
            for (y in 0 until towers[0].size) {
                g.drawLine(0, y * cellSize, towers.size * cellSize, y * cellSize)
            }
        }

        g.setColor(Color.black)
        //Drawing the Sprites:
        for (x in towers.indices) {
            for (y in 0 until towers[x].size) {
                val tower = towers[x][y]
                if (tower != null) tower!!.draw(g)
            }
        }

        for (i in spriteList.indices) {
            val sprite = spriteList[i]
            sprite.draw(g)
        }
    }

    companion object {

        private var towers: Array<Array<Tower>>
        private var spriteList: MutableList<Sprite>

        var cellSize: Int


        var buildphase = true
            private set
        var wave = 0

        var gold: Int = 0
        private val START_GOLD = 200


        var target: Target? = null
            set(pTarget) {
                field = pTarget
                addTower(this.target!!)
            }
        private var spawnPlace: SpawnPlace? = null
        private var travelDistance: Int = 0
        private val MIN_TRAVEL_DISTANCE = 8

        private val imageArray = arrayOfNulls<BufferedImage>(12)
        private val imageArrayFrozen = arrayOfNulls<BufferedImage>(12)

        private fun initialize() {
            gold = START_GOLD
            target = Target((Math.random() * Arena.width).toInt(), (Math.random() * Arena.height).toInt(), cellSize)
            var spawnX: Int
            var spawnY: Int

            do {
                val spawnSide = (Math.random() * 4 + 1).toInt()
                when (spawnSide) {
                    0 -> {
                        spawnX = (Math.random() * Arena.width).toInt()
                        spawnY = 0
                    }
                    1 -> {
                        spawnX = Arena.width - 1
                        spawnY = (Math.random() * Arena.height).toInt()
                    }
                    2 -> {
                        spawnX = (Math.random() * Arena.width).toInt()
                        spawnY = Arena.height - 1
                    }
                    3 -> {
                        spawnX = 0
                        spawnY = (Math.random() * Arena.height).toInt()
                    }
                    else -> {
                        spawnX = 0
                        spawnY = 0
                    }
                }
                travelDistance = Math.abs(spawnX - this.target!!.getCellX() + (spawnY - this.target!!.getCellY()))
            } while (travelDistance < MIN_TRAVEL_DISTANCE)

            spawnPlace = SpawnPlace(spawnX, spawnY, cellSize)
            addTower(spawnPlace!!)
        }


        fun reset() {

            for (x in towers.indices) {
                for (y in 0 until towers[x].size) {
                    if (towers[x][y] != null) {
                        towers[x][y].dispose()
                        towers[x][y] = null
                    }
                }
            }
            while (!spriteList.isEmpty()) {
                spriteList[0].dispose()
                spriteList.removeAt(0)
            }
            initialize()
            wave = 1
            buildphase = true
        }


        fun startWave() {


            removeTower(spawnPlace!!.getCellX(), spawnPlace!!.getCellY())
            val spawner: Spawner//= new Spawner(spawnPlace.getCellX(), spawnPlace.getCellY(), 1);
            buildphase = false

            val creeps = ArrayList<Creep>()

            when (wave) {
                0 -> {
                    println("wave0")
                    spawner = Spawner(spawnPlace!!.getCellX(), spawnPlace!!.getCellY(), 3)
                    creeps.add(Creep(cellSize, 5, 2, 1, 20, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 5, 2, 1, 20, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 5, 2, 1, 20, imageArray, imageArrayFrozen))
                    spawner.setCreepList(creeps)
                    addSprite(spawner)
                }
                1 -> {
                    spawner = Spawner(spawnPlace!!.getCellX(), spawnPlace!!.getCellY(), 3)
                    creeps.add(Creep(cellSize, 8, 2, 1, 50, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 8, 2, 1, 50, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 8, 2, 1, 50, imageArray, imageArrayFrozen))
                    spawner.setCreepList(creeps)
                    addSprite(spawner)
                }
                2 -> {
                    spawner = Spawner(spawnPlace!!.getCellX(), spawnPlace!!.getCellY(), 2.5f)
                    creeps.add(Creep(cellSize, 14, 2, 2, 50, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 14, 2, 2, 50, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 14, 2, 2, 50, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 14, 2, 2, 50, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 14, 2, 2, 50, imageArray, imageArrayFrozen))
                    spawner.setCreepList(creeps)
                    addSprite(spawner)
                }
                3 -> {
                    spawner = Spawner(spawnPlace!!.getCellX(), spawnPlace!!.getCellY(), 2.5f)
                    creeps.add(Creep(cellSize, 22, 2, 2, 60, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 22, 2, 2, 60, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 22, 2, 2, 60, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 22, 2, 2, 60, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 22, 2, 2, 60, imageArray, imageArrayFrozen))
                    spawner.setCreepList(creeps)
                    addSprite(spawner)
                }
                4 -> {
                    spawner = Spawner(spawnPlace!!.getCellX(), spawnPlace!!.getCellY(), 2f)
                    creeps.add(Creep(cellSize, 28, 2.2, 2, 70, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 28, 2.2, 2, 70, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 28, 2.2, 2, 70, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 28, 2.2, 2, 70, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 28, 2.2, 2, 70, imageArray, imageArrayFrozen))
                    spawner.setCreepList(creeps)
                    addSprite(spawner)
                }
                5 -> {
                    spawner = Spawner(spawnPlace!!.getCellX(), spawnPlace!!.getCellY(), 2f)
                    creeps.add(Creep(cellSize, 38, 2.3, 2, 80, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 38, 2.3, 2, 80, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 38, 2.3, 2, 80, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 38, 2.3, 2, 80, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 38, 2.3, 2, 80, imageArray, imageArrayFrozen))
                    spawner.setCreepList(creeps)
                    addSprite(spawner)
                }
                6 -> {
                    spawner = Spawner(spawnPlace!!.getCellX(), spawnPlace!!.getCellY(), 2f)
                    creeps.add(Creep(cellSize, 57, 2.4, 2, 90, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 57, 2.4, 2, 90, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 57, 2.4, 2, 90, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 57, 2.4, 2, 90, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 57, 2.4, 2, 90, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 57, 2.4, 2, 90, imageArray, imageArrayFrozen))
                    creeps.add(Creep(cellSize, 57, 2.4, 2, 90, imageArray, imageArrayFrozen))
                    spawner.setCreepList(creeps)
                    addSprite(spawner)
                }
                else -> buildphase = true
            }
        }


        fun isFree(x: Int, y: Int): Boolean {
            return towers[x][y] == null
        }


        fun towerAffordable(towerGold: Int): Boolean {
            return if (gold - towerGold < 0) {
                false
            } else true
        }

        fun targetReachable(): Boolean {
            removeTower(spawnPlace!!.getCellX(), spawnPlace!!.getCellY())
            val spawner = Spawner(spawnPlace!!.getCellX(), spawnPlace!!.getCellY(), 1)
            val reachable = spawner.findPath() != null
            addTower(spawnPlace!!)
            return reachable
        }

        fun addTower(tower: Tower) {
            towers[tower.cellX][tower.cellY] = tower
            gold = gold - tower.gold
        }

        fun removeTower(cellX: Int, cellY: Int) {
            towers[cellX][cellY] = null
        }

        fun getTower(pCellX: Int, pCellY: Int): Tower? {

            var tower: Tower? = null

            if (pCellX < towers.size && pCellY < towers[0].size)
                tower = towers[pCellX][pCellY]

            return tower
        }


        fun addSprite(sprite: Sprite) {
            spriteList.add(sprite)
        }


        fun getSpriteList(): List<Sprite> {
            val temp = ArrayList<Sprite>()
            temp.addAll(spriteList)
            return temp
        }

        val width: Int
            get() = towers.size

        val height: Int
            get() = towers[0].size


        fun waveFinished(): Boolean {
            return if (spriteList.isEmpty())
                true
            else
                false
        }

        fun setBuildPhase(pPhase: Boolean) {
            buildphase = pPhase
        }

        fun addGold(pGold: Int) {
            gold += pGold
        }
    }


}
