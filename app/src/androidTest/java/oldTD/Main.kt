import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException

import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JTextField

class Main : JFrame() {

    private var lastFrameMillis: Long = 0
    private var deltaTime: Float = 0.toFloat()

    private val screen: Screen

    private var arena: Arena? = null

    private val mouseControll = MouseControll()


    internal var layout1: BufferedImage? = null
    internal var layoutInfo1: BufferedImage? = null
    internal var layoutInfo2: BufferedImage? = null

    internal var ashe1: BufferedImage? = null
    internal var obstacle1: BufferedImage? = null
    internal var lux1: BufferedImage? = null
    internal var ali1: BufferedImage? = null
    internal var lee1: BufferedImage? = null
    internal var yi1: BufferedImage? = null
    internal var annie1: BufferedImage? = null
    internal var olaf1: BufferedImage? = null
    internal var jinx1: BufferedImage? = null
    internal var rumble1: BufferedImage? = null

    init {
        this.setSize(800, 900)
        this.setResizable(false)
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        this.setLocationRelativeTo(null)


        try {
            layout1 = ImageIO.read(File("img/layout/Layout1.png"))
            layoutInfo1 = ImageIO.read(File("img/layout/Layout_Info1.png"))
            layoutInfo2 = ImageIO.read(File("img/layout/Layout_Info2.png"))
            ashe1 = ImageIO.read(File("img/Ashe/Ashe1.png"))
            obstacle1 = ImageIO.read(File("img/obstacle.png"))
            lux1 = ImageIO.read(File("img/Lux/Lux1.png"))
            ali1 = ImageIO.read(File("img/Alistar/Ali1.png"))
            lee1 = ImageIO.read(File("img/Lee/Lee1.png"))
            yi1 = ImageIO.read(File("img/Yi/Yi1.png"))
            annie1 = ImageIO.read(File("img/Annie/Annie1.png"))
            olaf1 = ImageIO.read(File("img/Olaf/Olaf1.png"))
            jinx1 = ImageIO.read(File("img/Jinx/Jinx1.png"))
            rumble1 = ImageIO.read(File("img/Rumble/Rumble1.png"))

        } catch (e: IOException) {
            // e.printStackTrace();
            println("cant read input file")
        }


        screen = Screen()
        screen.setSize(this.getWidth(), this.getHeight())
        screen.addMouseListener(mouseControll)
        screen.addMouseMotionListener(mouseControll)
        JLabel1.setBounds(130, 820, 600, 40)
        JLabel1.setEditable(false)
        val font1 = Font("SansSerif", Font.BOLD, 20)
        JLabel1.setFont(font1)
        JLabel1.setForeground(Color.WHITE)
        JLabel1.setText("")
        JLabel1.setOpaque(false)
        screen.add(JLabel1)
        this.add(screen)

        this.setVisible(true)
    }

    fun startGameLoop() {
        initialize()

        while (true) {
            doLogic()

            draw()

            try {
                Thread.sleep(20)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    private fun initialize() {
        lastFrameMillis = System.currentTimeMillis()
        arena = Arena(16, 12, 50)
    }

    private fun doLogic() {
        computeDeltaTime()
        arena!!.doLogic(deltaTime)
        mouseControll.doLogic(deltaTime)
    }

    private fun draw() {
        screen.repaint()
    }

    private fun computeDeltaTime() {
        val currentTime = System.currentTimeMillis()
        deltaTime = (currentTime - lastFrameMillis) / 1000.toFloat()

        lastFrameMillis = currentTime
    }


    private inner class Screen : JLabel() {

        protected fun paintComponent(g: Graphics) {
            super.paintComponent(g)

            if (arena != null)
                arena!!.draw(g)

            g.drawImage(layout1, 0, 600, 800, 280, null)
            mouseControll.draw(g)
            g.setColor(Color.WHITE)
            g.drawImage(obstacle1, 183, 660, 50, 50, null)
            g.drawImage(lee1, 243, 660, 50, 50, null)
            g.drawImage(ashe1, 303, 660, 50, 50, null)
            g.drawImage(annie1, 368, 655, 50, 60, null)
            g.drawImage(lux1, 423, 660, 50, 50, null)
            g.drawImage(olaf1, 183, 720, 50, 50, null)
            g.drawImage(ali1, 243, 715, 50, 50, null)
            g.drawImage(rumble1, 303, 720, 50, 50, null)
            g.drawImage(jinx1, 366, 720, 55, 50, null)
            g.drawImage(yi1, 423, 720, 50, 50, null)

            g.drawString("start", 50, 845)
            if (Arena.getBuildphase() === true) {
                g.drawString("Build Phase " + Arena.getWave(), 50, 765)
            } else {
                g.drawString("Wave: " + Arena.getWave(), 50, 765)
            }
            g.setColor(Color.orange)
            g.drawString("Gold: " + Arena.getGold(), 40, 675)

            //			g.fillOval((int)(Math.random()*60)+400,
            //					(int)(Math.random()*80)+200, 300, 300);
        }

    }

    private inner class MouseControll : MouseMotionListener, MouseListener {

        private var x: Int = 0
        private var y: Int = 0
        private var buildTower = 0
        private var notEnoughGold = false
        private var notEnoughGold2 = false
        private var timer = 0f

        private var xCell: Int = 0
        private var yCell: Int = 0
        private var pressedButton: Int = 0

        internal var activeTower: Tower? = null

        fun doLogic(deltaTime: Float) {
            timer += deltaTime
            if (timer > 1.5) {
                timer = 0f
                notEnoughGold = false
                notEnoughGold2 = false
            }
        }

        fun draw(g: Graphics) {

            if (buildTower == 1) {
                g.drawImage(layoutInfo1, 490, 650, 280, 150, null)
                g.setColor(Color.WHITE)
                g.drawImage(obstacle1, x, y - 20, 50, 70, null)
                g.drawString(TowerObstacle.NAME, 510, 672)
                g.drawString("Damage: " + TowerObstacle.DAMAGE, 510, 692)
                g.drawString("Speed: " + TowerObstacle.SPEED, 510, 712)
                g.drawString("Range: " + TowerObstacle.RANGE, 510, 732)
                //				g.drawOval(x+Arena.getCellSize()/2-TowerObstacle.RANGE, y+Arena.getCellSize()/2-TowerObstacle.RANGE, TowerObstacle.RANGE*2, TowerObstacle.RANGE*2);
            } else if (buildTower == 2) {
                g.drawImage(layoutInfo1, 490, 650, 280, 150, null)
                g.drawImage(lee1, x, y - 20, 50, 66, null)
                g.setColor(Color.WHITE)
                g.drawString(TowerLee.NAME[0], 510, 672)
                g.drawString("Damage: " + TowerLee.DAMAGE[0], 510, 692)
                g.drawString("Speed: " + TowerLee.SPEED[0], 510, 712)
                g.drawString("Range: " + TowerLee.RANGE, 510, 732)
                g.drawOval(
                    x + Arena.getCellSize() / 2 - TowerLee.RANGE,
                    y + Arena.getCellSize() / 2 - TowerLee.RANGE,
                    TowerLee.RANGE * 2,
                    TowerLee.RANGE * 2
                )
            } else if (buildTower == 3) {
                g.drawImage(layoutInfo1, 490, 650, 280, 150, null)
                g.drawImage(ashe1, x, y - 20, 50, 70, null)
                g.setColor(Color.WHITE)
                g.drawString(TowerTorsten.NAME[0], 510, 672) // TODO
                g.drawString("Damage: " + TowerTorsten.DAMAGE[0], 510, 692)
                g.drawString("Speed: " + TowerTorsten.SPEED[0], 510, 712)
                g.drawString("Range: " + TowerTorsten.RANGE, 510, 732)
                g.drawOval(
                    x + Arena.getCellSize() / 2 - TowerTorsten.RANGE,
                    y + Arena.getCellSize() / 2 - TowerTorsten.RANGE,
                    TowerTorsten.RANGE * 2,
                    TowerTorsten.RANGE * 2
                )
            } else if (buildTower == 4) {
                g.drawImage(layoutInfo1, 490, 650, 280, 150, null)
                g.drawImage(annie1, x, y - 20, 50, 66, null)
                g.setColor(Color.WHITE)
                g.drawString(TowerAnnie.NAME[0], 510, 672)
                g.drawString("Damage: " + TowerAnnie.DAMAGE[0], 510, 692)
                g.drawString("Speed: " + TowerAnnie.SPEED[0], 510, 712)
                g.drawString("Range: " + TowerAnnie.RANGE, 510, 732)
                g.drawOval(
                    x + Arena.getCellSize() / 2 - TowerAnnie.RANGE,
                    y + Arena.getCellSize() / 2 - TowerAnnie.RANGE,
                    TowerAnnie.RANGE * 2,
                    TowerAnnie.RANGE * 2
                )
            } else if (buildTower == 5) {
                g.drawImage(layoutInfo1, 490, 650, 280, 150, null)
                g.drawImage(lux1, x, y - 20, 50, 70, null)
                g.setColor(Color.WHITE)
                g.drawString(TowerLux.NAME, 510, 672)
                g.drawString("Damage: " + TowerLux.DAMAGE, 510, 692)
                g.drawString("Speed: " + TowerLux.SPEED, 510, 712)
                g.drawString("Range: " + TowerLux.RANGE, 510, 732)
                g.drawOval(
                    x + Arena.getCellSize() / 2 - TowerLux.RANGE,
                    y + Arena.getCellSize() / 2 - TowerLux.RANGE,
                    TowerLux.RANGE * 2,
                    TowerLux.RANGE * 2
                )
            } else if (buildTower == 6) {
                g.drawImage(layoutInfo1, 490, 650, 280, 150, null)
                g.drawImage(olaf1, x, y - 20, 50, 66, null)
                g.setColor(Color.WHITE)
                g.drawString(TowerOlaf.NAME, 510, 672)
                g.drawString("Damage: " + TowerOlaf.DAMAGE, 510, 692)
                g.drawString("Speed: " + TowerOlaf.SPEED, 510, 712)
                g.drawString("Range: " + TowerOlaf.RANGE, 510, 732)
                g.drawOval(
                    x + Arena.getCellSize() / 2 - TowerOlaf.RANGE,
                    y + Arena.getCellSize() / 2 - TowerOlaf.RANGE,
                    TowerOlaf.RANGE * 2,
                    TowerOlaf.RANGE * 2
                )
            } else if (buildTower == 7) {
                g.drawImage(layoutInfo1, 490, 650, 280, 150, null)
                g.drawImage(ali1, x, y - 20, 50, 66, null)
                g.setColor(Color.WHITE)
                g.drawString(TowerAlistar.NAME, 510, 672)
                g.drawString("Damage: " + TowerAlistar.DAMAGE, 510, 692)
                g.drawString("Speed: " + TowerAlistar.SPEED, 510, 712)
                g.drawString("Range: " + TowerAlistar.RANGE, 510, 732)
                g.drawOval(
                    x + Arena.getCellSize() / 2 - TowerAlistar.RANGE,
                    y + Arena.getCellSize() / 2 - TowerAlistar.RANGE,
                    TowerAlistar.RANGE * 2,
                    TowerAlistar.RANGE * 2
                )
            } else if (buildTower == 8) {
                g.drawImage(layoutInfo1, 490, 650, 280, 150, null)
                g.drawImage(rumble1, x, y - 20, 55, 66, null)
                g.setColor(Color.WHITE)
                g.drawString(TowerRumble.NAME, 510, 672)
                g.drawString("Damage: " + TowerRumble.DAMAGE, 510, 692)
                g.drawString("Speed: " + TowerRumble.SPEED, 510, 712)
                g.drawString("Range: " + TowerRumble.RANGE, 510, 732)
                g.drawOval(
                    x + Arena.getCellSize() / 2 - TowerRumble.RANGE,
                    y + Arena.getCellSize() / 2 - TowerRumble.RANGE,
                    TowerRumble.RANGE * 2,
                    TowerRumble.RANGE * 2
                )
            } else if (buildTower == 9) {
                g.drawImage(layoutInfo1, 490, 650, 280, 150, null)
                g.drawImage(jinx1, x, y - 20, 55, 66, null)
                g.setColor(Color.WHITE)
                g.drawString(TowerJinx.NAME, 510, 672)
                g.drawString("Damage: " + TowerJinx.DAMAGE, 510, 692)
                g.drawString("Speed: " + TowerJinx.SPEED, 510, 712)
                g.drawString("Range: " + TowerJinx.RANGE, 510, 732)
                g.drawOval(
                    x + Arena.getCellSize() / 2 - TowerJinx.RANGE,
                    y + Arena.getCellSize() / 2 - TowerJinx.RANGE,
                    TowerJinx.RANGE * 2,
                    TowerJinx.RANGE * 2
                )
            } else if (buildTower == 10) {
                g.drawImage(layoutInfo1, 490, 650, 280, 150, null)
                g.drawImage(yi1, x, y - 20, 50, 66, null)
                g.setColor(Color.WHITE)
                g.drawString(TowerYi.NAME, 510, 672)
                g.drawString("Damage: " + TowerYi.DAMAGE, 510, 692)
                g.drawString("Speed: " + TowerYi.SPEED, 510, 712)
                g.drawString("Range: " + TowerYi.RANGE, 510, 732)
                g.drawOval(
                    x + Arena.getCellSize() / 2 - TowerYi.RANGE,
                    y + Arena.getCellSize() / 2 - TowerYi.RANGE,
                    TowerYi.RANGE * 2,
                    TowerYi.RANGE * 2
                )
            } else if (activeTower != null && !(activeTower is SpawnPlace || activeTower is Target)) {
                //TODO Hier weitermachen!
                g.drawImage(layoutInfo2, 490, 650, 280, 150, null)
                g.setColor(Color.WHITE)
                g.drawString(activeTower!!.name, 510, 672)
                g.drawString("Damage: " + activeTower!!.damage, 510, 692)
                g.drawString("Speed: " + activeTower!!.speed, 510, 712)
                g.drawString("Range: " + activeTower!!.range, 510, 732)
                g.drawString("upgrade", 653, 783)
                g.drawString("sell", 575, 783)
                g.drawOval(
                    activeTower!!.getX() + Arena.getCellSize() / 2 - activeTower!!.range,
                    activeTower!!.getY() + Arena.getCellSize() / 2 - activeTower!!.range,
                    activeTower!!.range * 2,
                    activeTower!!.range * 2
                )
            }
            if (notEnoughGold) {
                g.setColor(Color.orange)
                g.drawString("Nicht genug Gold", x - 20, y - 50)

            }
            if (notEnoughGold2) {
                g.setColor(Color.orange)
                g.drawString("Nicht genug Gold", 450, 640)

            }
        }

        fun mouseClicked(e: MouseEvent) {
            // TODO Auto-generated method stub

        }

        fun mousePressed(e: MouseEvent) {
            pressedButton = e.getButton()
            xCell = e.getX() / 50
            yCell = e.getY() / 50

            if (Arena.getTower(xCell, yCell) != null) {
                activeTower = Arena.getTower(xCell, yCell)
            } else if ((e.getX() > 490 && e.getX() < 770 && e.getY() > 650 && e.getY() < 800) == false) {
                activeTower = null
            }

            if (activeTower != null) {

                if (e.getX() > 642 && e.getX() < 715 && e.getY() > 770 && e.getY() < 790) {
                    if (Arena.getGold() - activeTower!!.upgradeCost >= 0) {
                        activeTower!!.upgrade()
                    } else {
                        setMessageBoard("nicht genug Gold")
                        notEnoughGold2 = true
                        timer = 0f
                    }

                } else if (e.getX() > 550 && e.getX() > 570 && e.getY() > 770 && e.getY() < 790) {
                    Arena.removeTower(activeTower!!.cellX, activeTower!!.cellY)
                    Arena.addGold(activeTower!!.gold / 2)
                    activeTower = null
                }
            }
            if (Arena.getBuildphase() === true) {

                if (xCell < 16 && yCell < 12) {
                    if (e.getButton() === MouseEvent.BUTTON3) {
                        val temp = Arena.getTower(xCell, yCell)
                        if (temp != null && !(temp is SpawnPlace || temp is Target)) {
                            Arena.removeTower(xCell, yCell)
                            Arena.addGold(temp!!.gold / 2)
                            activeTower = null
                            return
                        }
                    } else {
                        if (buildTower != 0 && !Arena.isFree(xCell, yCell)) {
                            setMessageBoard("Feld Besetzt")
                            return
                        }
                        if (buildTower > 0) {
                            buildTower(xCell, yCell)
                        }
                        if (!Arena.targetReachable()) {
                            Arena.addGold(Arena.getTower(xCell, yCell).gold)
                            Arena.removeTower(xCell, yCell)
                            setMessageBoard("Weg darf nicht versperrt werden!")
                        }
                    }
                }
                if (e.getX() > 183 && e.getX() < 233 && e.getY() > 660 && e.getY() < 710) {
                    if (buildTower != 1) {
                        buildTower = 1
                    } else {
                        buildTower = 0
                    }
                } else if (e.getX() > 243 && e.getX() < 293 && e.getY() > 660 && e.getY() < 710) {
                    if (buildTower != 2) {
                        buildTower = 2
                    } else {
                        buildTower = 0
                    }
                } else if (e.getX() > 303 && e.getX() < 353 && e.getY() > 660 && e.getY() < 710) {
                    if (buildTower != 3) {
                        buildTower = 3
                    } else {
                        buildTower = 0
                    }
                } else if (e.getX() > 363 && e.getX() < 423 && e.getY() > 660 && e.getY() < 710) {
                    if (buildTower != 4) {
                        buildTower = 4
                    } else {
                        buildTower = 0
                    }
                } else if (e.getX() > 423 && e.getX() < 483 && e.getY() > 660 && e.getY() < 710) {
                    if (buildTower != 5) {
                        buildTower = 5
                    } else {
                        buildTower = 0
                    }
                } else if (e.getX() > 183 && e.getX() < 233 && e.getY() > 720 && e.getY() < 770) {
                    if (buildTower != 6) {
                        buildTower = 6
                    } else {
                        buildTower = 0
                    }
                } else if (e.getX() > 243 && e.getX() < 293 && e.getY() > 720 && e.getY() < 770) {
                    if (buildTower != 7) {
                        buildTower = 7
                    } else {
                        buildTower = 0
                    }
                } else if (e.getX() > 303 && e.getX() < 353 && e.getY() > 720 && e.getY() < 770) {
                    if (buildTower != 8) {
                        buildTower = 8
                    } else {
                        buildTower = 0
                    }
                } else if (e.getX() > 363 && e.getX() < 413 && e.getY() > 720 && e.getY() < 770) {
                    if (buildTower != 9) {
                        buildTower = 9
                    } else {
                        buildTower = 0
                    }
                } else if (e.getX() > 423 && e.getX() < 473 && e.getY() > 720 && e.getY() < 770) {
                    if (buildTower != 10) {
                        buildTower = 10
                    } else {
                        buildTower = 0
                    }
                }
            }
            if (e.getX() > 27 && e.getX() < 102 && e.getY() > 825 && e.getY() < 855) {
                buildTower = 0
                if (Arena.getBuildphase()) {
                    setMessageBoard("")
                    Arena.startWave()
                }
            }

        }

        fun mouseReleased(e: MouseEvent) {
            // TODO Auto-generated method stub

        }

        fun mouseEntered(e: MouseEvent) {
            // TODO Auto-generated method stub

        }

        fun mouseExited(e: MouseEvent) {
            // TODO Auto-generated method stub

        }

        fun mouseDragged(e: MouseEvent) {

            xCell = e.getX() / 50
            yCell = e.getY() / 50


            if (pressedButton == MouseEvent.BUTTON1) {

                if (xCell >= 0 && xCell < 16 && yCell >= 0 && yCell < 12) {
                    if (buildTower != 0 && !Arena.isFree(xCell, yCell)) {
                        return
                    }

                    if (buildTower > 0) {
                        buildTower(xCell, yCell)
                    }
                    if (!Arena.targetReachable()) {
                        Arena.addGold(Arena.getTower(xCell, yCell).gold)
                        Arena.removeTower(xCell, yCell)
                        setMessageBoard("Weg darf nicht versperrt werden!")
                    }
                }
            }

        }


        private fun buildTower(x: Int, y: Int) {
            val tower: Tower?
            when (buildTower) {
                1 -> tower = TowerObstacle(x, y, Arena.getCellSize())
                2 -> tower = TowerLee(x, y, Arena.getCellSize())
                3 -> tower = TowerTorsten(x, y, Arena.getCellSize())
                4 -> tower = TowerAnnie(x, y, Arena.getCellSize())
                5 -> tower = TowerLux(x, y, Arena.getCellSize())
                6 -> tower = TowerOlaf(x, y, Arena.getCellSize())
                7 -> tower = TowerAlistar(x, y, Arena.getCellSize())
                8 -> tower = TowerRumble(x, y, Arena.getCellSize())
                9 -> tower = TowerJinx(x, y, Arena.getCellSize())
                10 -> tower = TowerYi(x, y, Arena.getCellSize())
                else -> tower = null
            }
            if (tower != null) {
                if (Arena.towerAffordable(tower!!.gold)) {
                    Arena.addTower(tower)
                } else {
                    timer = 0f
                    notEnoughGold = true
                    setMessageBoard("nicht genug Gold")
                }
            }
        }


        fun mouseMoved(e: MouseEvent) {
            // TODO Auto-generated method stub


            if (e.getX() > 183 && e.getX() < 233 && e.getY() > 660 && e.getY() < 710) {
                setMessageBoard("Kosten:  " + TowerObstacle.GOLD)
            } else if (e.getX() > 243 && e.getX() < 293 && e.getY() > 660 && e.getY() < 710) {
                setMessageBoard("Kosten:  " + TowerLee.GOLD)
            } else if (e.getX() > 303 && e.getX() < 353 && e.getY() > 660 && e.getY() < 710) {
                setMessageBoard("Kosten:  " + TowerTorsten.GOLD)
            } else if (e.getX() > 363 && e.getX() < 423 && e.getY() > 660 && e.getY() < 710) {
                setMessageBoard("Kosten:  " + TowerAnnie.GOLD)
            } else if (e.getX() > 423 && e.getX() < 483 && e.getY() > 660 && e.getY() < 710) {
                setMessageBoard("Kosten:  " + TowerLux.GOLD)
            } else if (e.getX() > 183 && e.getX() < 233 && e.getY() > 720 && e.getY() < 770) {
                setMessageBoard("Kosten:  " + TowerOlaf.GOLD)
            } else if (e.getX() > 243 && e.getX() < 293 && e.getY() > 720 && e.getY() < 770) {
                setMessageBoard("Kosten:  " + TowerAlistar.GOLD)
            } else if (e.getX() > 303 && e.getX() < 353 && e.getY() > 720 && e.getY() < 770) {
                setMessageBoard("Kosten:  " + TowerRumble.GOLD)
            } else if (e.getX() > 363 && e.getX() < 413 && e.getY() > 720 && e.getY() < 770) {
                setMessageBoard("Kosten:  " + TowerJinx.GOLD)
            } else if (e.getX() > 423 && e.getX() < 473 && e.getY() > 720 && e.getY() < 770) {
                setMessageBoard("Kosten:  " + TowerYi.GOLD)
            }


            if (e.getY() < 600) {
                val xCell = e.getX() / 50
                val yCell = e.getY() / 50
                x = xCell * 50
                y = yCell * 50
            } else {
                x = e.getX()
                y = e.getY()
            }
        }
    }

    companion object {

        private val JLabel1 = JTextField()


        @JvmStatic
        fun main(args: Array<String>) {
            val main = Main()

            main.startGameLoop()
        }

        fun setMessageBoard(pMessage: String) {
            JLabel1.setText(pMessage)
        }
    }
}