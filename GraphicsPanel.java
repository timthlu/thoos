import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

//FOR COLOURS https://www.rapidtables.com/web/color/RGB_Color.html
public class GraphicsPanel extends JPanel {
    private Player player1 = new Player(new Point(80, 280), new Point(-1, 0), Color.blue);
    private Player player2 = new Player(new Point(1080, 280), new Point(1, 0), Color.red);
    private Rectangle playerBound = new Rectangle(88, 88, 1024, 424);
    private Rectangle bulletBound = new Rectangle(60, 60, 1080, 480);
    private boolean[][] walls;
    private ArrayList<Rectangle> wallHitBoxes;
    private Map map = new Map();
    private Clock clock = new Clock();
    private FrameRate fr = new FrameRate();
    private MovementClock mc = new MovementClock();
    private JFrame gameFrame;

    public GraphicsPanel(JFrame gameFrame) {
        MyKeyListener keyListener = new MyKeyListener();
        addKeyListener(keyListener);
        setFocusable(true);
        Bullet.init();
        Pickup.init();
        this.gameFrame = gameFrame;
        walls = map.getMap();
        wallHitBoxes = map.getWallHitBoxes();

        Thread t = new Thread(new Runnable() {
            public void run() {
                animate();
            }
        });
        t.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBackground(g);
        paintPickups(g);
        paintBullets(g);
        paintWallObstacles(g);
        paintPlayer(g, player1);
        paintPlayer(g, player2);
        paintHealthBars(g);
        paintAmmoLeft(g);
        if (player1.getIsReloading()) {
            paintReloadTime(g, player1);
        }
        if (player2.getIsReloading()) {
            paintReloadTime(g, player2);
        }
        paintLives(g);
        drawFrameRate(g);
    }

    private void paintBackground(Graphics g) {
        super.paintComponent(g);

        ////////////////// Background Graphics //////////////////
        // playing field graphics
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1200, 600);
        g.setColor(Color.WHITE);
        g.fillRect(50, 50, 1100, 500);

        // colours
        Color paleBlue = new Color(172, 162, 255);
        Color paleRed = new Color(255, 162, 162);
        Color paleGreen = new Color(165, 255, 162);
        Color paleYellow = new Color(255, 255, 162);
        Color paleCyan = new Color(164, 255, 255);

        // lower display pale background graphics
        g.setColor(paleBlue);
        g.fillRect(0, 600, 550, 200);
        g.setColor(paleRed);
        g.fillRect(650, 600, 550, 200);
        g.setColor(paleGreen);
        g.fillRect(0, 625, 1200, 28);
        g.setColor(paleYellow);
        g.fillRect(0, 675, 1200, 28);
        g.setColor(paleCyan);
        g.fillRect(0, 725, 1200, 28);

        // text box colours
        g.setColor(Color.black);
        g.fillRect(550, 600, 100, 200);
        g.setColor(Color.green);
        g.fillRect(550, 625, 100, 28);
        g.setColor(Color.yellow);
        g.fillRect(550, 675, 100, 28);
        g.setColor(Color.cyan);
        g.fillRect(550, 725, 100, 28);

        // border
        g.setColor(Color.lightGray);
        for (int i = 0; i < 5; i++) {
            g.draw3DRect(550 - i, 604 - i, 100 + 2 * i - 1, 163 + 2 * i - 1, true);
        }

        // drawing strings
        g.setColor(Color.black);
        Font standardFont = new Font("SansSerif", Font.BOLD, 16);
        g.setFont(standardFont);
        int healthWidth = g.getFontMetrics().stringWidth("Health");
        g.drawString("Health", Math.round(50 - healthWidth / 2) + 550, 645);
        int ammoWidth = g.getFontMetrics().stringWidth("Ammo");
        g.drawString("Ammo", Math.round(50 - ammoWidth / 2) + 550, 695);
        int livesWidth = g.getFontMetrics().stringWidth("Lives");
        g.drawString("Lives", Math.round(50 - livesWidth / 2) + 550, 745);
    }

    private void paintPickups(Graphics g) {
        ////////////////// Paint and generate pickups //////////////////
        Pickup pickup;
        for (int i = 0; i < Pickup.getInstances().size(); i++) {
            pickup = Pickup.getInstances().get(i);
            if (pickup.getDurationLeft() < 7000) {
                g.setColor(pickup.getColor());
            } else {
                g.setColor(pickup.getFadedColor());
            }
            g.fillRect((int) pickup.getPos().x, (int) pickup.getPos().y, 20, 20);
        }
    }

    private void paintWallObstacles(Graphics g) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 22; j++) {
                if (walls[i][j]) {
                    g.setColor(Color.black);
                    g.fillRect(50 + (j * 50), 50 + (i * 50), 50, 50);
                }
            }
        }
    }

    private void paintPlayer(Graphics g, Player player) {
        //base
        if (player.getIsRespawning()) {
            if (System.currentTimeMillis() - player.getTimeRespawnStart() < 2000) {
                g.setColor(new Color(255, 255, 0, 175));
            } else {
                g.setColor(new Color(255, 255, 0, 100));
            }
            g.fillOval((int) player.getPos().x - 15, (int) player.getPos().y - 15, 70, 70);
        } else if (player.getSpeedTime() != 0 || player.getDamageTime() != 0) {
            if (player.getSpeedTime() != 0) {
                if (System.currentTimeMillis() - player.getSpeedTime() < 27000) {
                    g.setColor(new Color(0, 255, 255, 175));
                } else {
                    g.setColor(new Color(0, 255, 255, 100));
                }
                g.fillOval((int) player.getPos().x - 15, (int) player.getPos().y - 15, 70, 70);
            }
            if (player.getDamageTime() != 0) {
                if (System.currentTimeMillis() - player.getDamageTime() < 27000) {
                    g.setColor(new Color(255, 0, 255, 175));
                } else {
                    g.setColor(new Color(255, 0, 255, 100));
                }
                g.fillOval((int) player.getPos().x - 15, (int) player.getPos().y - 15, 70, 70);
            }
        }
        g.setColor(player.getColor());
        g.fillRect((int) player.getPos().x, (int) player.getPos().y, player.getSize(), player.getSize());

        // cannon
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(4));
        g2.drawLine((int) player.getPos().x + player.getSize() / 2, (int) player.getPos().y + player.getSize() / 2,
                (int) player.getPos().x + player.getSize() / 2 + (int) player.getGunDir().x * player.getSize() / 2,
                (int) player.getPos().y + player.getSize() / 2 + (int) player.getGunDir().y * player.getSize() / 2);
    }

    private void paintBullets(Graphics g) {
        Bullet bullet;
        for (int i = 0; i < Bullet.getInstances().size(); ++i) {
            bullet = Bullet.getInstances().get(i);
            g.setColor(bullet.getColor());
            g.fillRect((int) bullet.getPos().x - bullet.getSize() / 2, (int) bullet.getPos().y - bullet.getSize() / 2, bullet.getSize(), bullet.getSize());
        }
    }

    private void paintHealthBars(Graphics g) {
        g.setColor(Color.green);
        for (int i = 0; i < 5; i++) {
            g.draw3DRect(25 - i, 631 - i, 500 + 2 * i - 1, 15 + 2 * i - 1, true);
        }
        g.fillRect(25, 631, player1.getHealth() * 5 * 100 / player1.getMaxHealth(), 15);

        for (int i = 0; i < 5; i++) {
            g.draw3DRect(675 - i, 631 - i, 500 + 2 * i - 1, 15 + 2 * i - 1, true);
        }
        g.fillRect(1175 - player2.getHealth() * 5 * 100 / player2.getMaxHealth(), 631,
                player2.getHealth() * 5 * 100 / player2.getMaxHealth(), 15);
    }

    private void paintAmmoLeft(Graphics g) {
        g.setColor(Color.orange);
        for (int i = 0; i < player1.getAmmo(); i++) {
            g.fillRect(30 + i * 15, 679, 10, 18);
        }
        for (int i = 0; i < 5; i++) {
            g.draw3DRect(25 - i, 681 - i, 125 + 2 * i - 1, 15 + 2 * i - 1, true);
        }

        for (int i = player2.getAmmo(); i > 0; i--) {
            g.fillRect(1175 - i * 15, 679, 10, 18);
        }
        for (int i = 0; i < 5; i++) {
            g.draw3DRect(1050 - i, 681 - i, 125 + 2 * i - 1, 15 + 2 * i - 1, true);
        }
    }

    private void paintReloadTime(Graphics g, Player player) {
        g.setColor(Color.orange);
        int currentReloadTime = (int) System.currentTimeMillis() - (int) player.getTimeReloadStart();
        if (player == player1 && currentReloadTime <= 1500) {
            for (int i = 0; i < 5; i++) {
                g.draw3DRect(375 - i, 683 - i, 150 + 2 * i - 1, 10 + 2 * i - 1, true);
            }
            g.fillRect(375, 683, currentReloadTime / 10, 10);
        } else if (currentReloadTime <= 1500) {
            for (int i = 0; i < 5; i++) {
                g.draw3DRect(675 - i, 683 - i, 150 + 2 * i - 1, 10 + 2 * i - 1, true);
            }
            g.fillRect(825 - currentReloadTime / 10, 683, currentReloadTime / 10, 10);
        }
    }

    private void paintLives(Graphics g) {
        g.setColor(Color.red);
        for (int i = 0; i < player1.getLives(); i++) {
            drawHeart(g, 25 + i * 50, 729, 20, 20);
        }

        for (int i = 0; i < player2.getLives(); i++) {
            drawHeart(g, 1155 - i * 50, 729, 20, 20);
        }
    }

    private void drawHeart(Graphics g, int x, int y, int width, int height) {
        int[] triangleX = {x - 2 * width / 18, x + width + 2 * width / 18,
                (x - 2 * width / 18 + x + width + 2 * width / 18) / 2};
        int[] triangleY = {y + height - 2 * height / 3, y + height - 2 * height / 3, y + height};
        g.fillOval(x - width / 12, y, width / 2 + width / 6, height / 2);
        g.fillOval(x + width / 2 - width / 12, y, width / 2 + width / 6, height / 2);
        g.fillPolygon(triangleX, triangleY, triangleX.length);
    }

    private void drawFrameRate(Graphics g) {
        fr.update();
        g.setColor(Color.white);
        fr.draw(g, 20, 20);
    }

    /**
     * METHODS
     **/
    public void animate() {
        while (true) {
            mc.update();
            // player movement when not reloading, player and collisions with
            // walls and obstacles (considered "out of bounds")
            if (!player1.getIsReloading()) {
                // System.out.println("Player 1 is not reloading");
                double player1ElapsedTime = mc.getElapsedTime();
                player1.move(player1ElapsedTime); // pos changes, lastPos does
                // not change
                keyPlayerInBounds(player1, player1ElapsedTime); // check if
                // player is out
                // of bounds,
                // then change pos or lastPos
            }
            checkPickupCollisions(player1);
            checkBuffDuration(player1);
            if (!player2.getIsReloading()) {
                // System.out.println("Player 2 is not reloading");
                double player2ElapsedTime = mc.getElapsedTime();
                player2.move(player2ElapsedTime);
                keyPlayerInBounds(player2, player2ElapsedTime);
            }
            checkPickupCollisions(player2);
            checkBuffDuration(player2);

            Bullet bullet;
            bulletCollision:
            for (int i = 0; i < Bullet.getInstances().size(); ++i) {
                bullet = Bullet.getInstances().get(i);
                bullet.move(mc.getElapsedTime());

                // Bullets and collisions
                if (bullet.getHitBox().intersects(player1.getHitBox()) && bullet.getPlayerShot() == 2) {
                    if (!player1.getIsRespawning()) {
                        player1.reduceHealth(bullet.getDamage());
                    }
                    Bullet.getInstances().remove(i);
                    i--;
                    continue;
                } else if (bullet.getHitBox().intersects(player2.getHitBox()) && bullet.getPlayerShot() == 1
                        && !player2.getIsRespawning()) {
                    if (!player2.getIsRespawning()) {
                        player2.reduceHealth(bullet.getDamage());
                    }
                    Bullet.getInstances().remove(i);
                    i--;
                    continue;
                }

                if (!bullet.getHitBox().intersects(bulletBound)) {
                    Bullet.getInstances().remove(i);
                    i--;
                    continue;
                }

                for (int j = 0; j < wallHitBoxes.size(); j++) {
                    if (bullet.getHitBox().intersects(wallHitBoxes.get(j))) {
                        Bullet.getInstances().remove(i);
                        i--;
                        continue bulletCollision;
                    }
                }
            }

            spawnDespawnPickups();

            if (player1.getHealth() <= 0 || player1.getIsRespawning()) {
                if (!player1.getIsRespawning()) {
                    respawn(player1);
                } else if (System.currentTimeMillis() - player1.getTimeRespawnStart() > 3000) {
                    player1.setIsRespawning(false);
                }
            }
            if (player2.getHealth() <= 0 || player2.getIsRespawning()) {
                if (!player2.getIsRespawning()) {
                    respawn(player2);
                } else if (System.currentTimeMillis() - player2.getTimeRespawnStart() > 3000) {
                    player2.setIsRespawning(false);
                }
            }

            if (player1.getLives() == 0) {
                // print player 1 wins on the game over screen
                gameFrame.dispose();
                Sound.playClip(new File("playerDeath.wav"));
                new GameOver(2);
                break;

            } else if (player2.getLives() == 0) {
                // print player 2 wins on the game over screen
                gameFrame.dispose();
                Sound.playClip(new File("playerDeath.wav"));
                new GameOver(1);
                break;
            }
            this.repaint();
        }
    }

    // for collisions
    private void keyPlayerInBounds(Player player, double elapsedTime) {
        boolean intersectingObstacle = false;
        int indexCount = 0;
        while (!intersectingObstacle && indexCount < wallHitBoxes.size()) {
            if (player.getHitBox().intersects(wallHitBoxes.get(indexCount))) {
                intersectingObstacle = true;
            }
            indexCount++;
        }
        if (player == player1) {
            if (!player.getHitBox().intersects(playerBound) || intersectingObstacle || player.getHitBox().intersects(player2.getHitBox())) { //player is out of bounds
                Point dir = player.getDir();
                double x = dir.x;
                double y = dir.y;
                boolean inObstacle = false;
                if (x != 0 && y != 0) {
                    double dirOrigX = x;
                    player.setDirX(0);
                    player.setPos(player.getLastPos());
                    player.move(elapsedTime);
                    indexCount = 0;
                    while (!inObstacle && indexCount < wallHitBoxes.size()) {
                        if (player.getHitBox().intersects(wallHitBoxes.get(indexCount))) {
                            inObstacle = true;
                        }
                        indexCount++;
                    }
                    if (!player.getHitBox().intersects(playerBound) || inObstacle || player.getHitBox().intersects(player2.getHitBox())) {
                        player.setDirX(dirOrigX);
                        player.setDirY(0);
                        player.setPos(player.getLastPos());
                        player.move(elapsedTime);
                        inObstacle = false;
                        indexCount = 0;
                        while (!inObstacle && indexCount < wallHitBoxes.size()) {
                            if (player.getHitBox().intersects(wallHitBoxes.get(indexCount))) {
                                inObstacle = true;
                            }
                            indexCount++;
                        }
                        if (!player.getHitBox().intersects(playerBound) || inObstacle || player.getHitBox().intersects(player2.getHitBox())) {
                            player.setPos(player.getLastPos());
                            player.setHitBox(new Rectangle((int) player.getPos().x, (int) player.getPos().y, player.getSize(), player.getSize()));
                        } else {
                            player.setPos(player.getLastPos());
                            player.move(elapsedTime);
                        }
                    } else {
                        player.setPos(player.getLastPos());
                        player.move(elapsedTime);
                    }
                } else {
                    player.setPos(player.getLastPos());
                    player.setHitBox(new Rectangle((int) player.getPos().x, (int) player.getPos().y, player.getSize(), player.getSize()));
                }

            } else { //player is in bounds
                player.setLastPos(player.getPos());
            }
        } else {
            if (!player.getHitBox().intersects(playerBound) || intersectingObstacle || player.getHitBox().intersects(player1.getHitBox())) { //player is out of bounds
                Point dir = player.getDir();
                double x = dir.x;
                double y = dir.y;
                boolean inObstacle = false;
                if (x != 0 && y != 0) {
                    double dirOrigX = x;
                    player.setDirX(0);
                    player.setPos(player.getLastPos());
                    player.move(elapsedTime);
                    indexCount = 0;
                    while (!inObstacle && indexCount < wallHitBoxes.size()) {
                        if (player.getHitBox().intersects(wallHitBoxes.get(indexCount))) {
                            inObstacle = true;
                        }
                        indexCount++;
                    }
                    if (!player.getHitBox().intersects(playerBound) || inObstacle || player.getHitBox().intersects(player1.getHitBox())) {
                        player.setDirX(dirOrigX);
                        player.setDirY(0);
                        player.setPos(player.getLastPos());
                        player.move(elapsedTime);
                        inObstacle = false;
                        indexCount = 0;
                        while (!inObstacle && indexCount < wallHitBoxes.size()) {
                            if (player.getHitBox().intersects(wallHitBoxes.get(indexCount))) {
                                inObstacle = true;
                            }
                            indexCount++;
                        }
                        if (!player.getHitBox().intersects(playerBound) || inObstacle || player.getHitBox().intersects(player1.getHitBox())) {
                            player.setPos(player.getLastPos());
                            player.setHitBox(new Rectangle((int) player.getPos().x, (int) player.getPos().y, player.getSize(), player.getSize()));
                        } else {
                            player.setPos(player.getLastPos());
                            player.move(elapsedTime);
                        }
                    } else {
                        player.setPos(player.getLastPos());
                        player.move(elapsedTime);
                    }
                } else {
                    player.setPos(player.getLastPos());
                    player.setHitBox(new Rectangle((int) player.getPos().x, (int) player.getPos().y, player.getSize(), player.getSize()));
                }

            } else { //player is in bounds
                player.setLastPos(player.getPos());
            }
        }
    }

    public void checkPickupCollisions(Player player) {
        Pickup pickup;
        for (int i = 0; i < Pickup.getInstances().size(); i++) {
            pickup = Pickup.getInstances().get(i);
            if (player.getHitBox().intersects(pickup.getHitBox())) {
                if (pickup instanceof Speed) {
                    // boost player speed
                    player.startSpeedPickup();
                } else if (pickup instanceof Health) {
                    // boost player health
                    player.useHealthPickup();
                    Sound.playClip(new File("healthPickup.wav"));
                } else {
                    // boost player damage
                    player.startDamagePickup();
                }

                Pickup.getInstances().remove(i);
            }
        }
    }

    public void spawnDespawnPickups() {
        if (clock.update()) {
            map.generatePickups();
        }

        Pickup pickup;
        for (int i = 0; i < Pickup.getInstances().size(); i++) {
            pickup = Pickup.getInstances().get(i);
            if (pickup.getDurationLeft() > 10000) {
                Pickup.getInstances().remove(i);
            }
        }
    }

    public void checkBuffDuration(Player player) {
        if (player.getSpeedTime() != 0 && System.currentTimeMillis() - player.getSpeedTime() > 30000) {
            player.endSpeedPickup();
        }
        if (player.getDamageTime() != 0 && System.currentTimeMillis() - player.getDamageTime() > 30000) {
            player.endDamagePickup();
        }
    }

    // for respawning
    private void respawn(Player player) {
        if (player == player1) {
            player.setPos(new Point(80, 280));
            player.setGunDir(new Point(-1, 0));
        } else {
            player.setPos(new Point(1080, 280));
            player.setGunDir(new Point(1, 0));
        }
        player.setHealth(player.getMaxHealth());
        player.refillAmmo();
        player.loseLife();

        Pickup.clearPickups();
        player1.clearBuffs();
        player2.clearBuffs();

        player.setIsRespawning(true);
        player.setTimeRespawnStart(System.currentTimeMillis());
    }

    /**
     * LISTENER SUBCLASS
     **/
    // listeners
    private class MyKeyListener implements KeyListener {
        public void keyTyped(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) { // TODO: change if statements to
            // match conventions *after*
            // project is done

            movePlayer(e, player1, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D);
            movePlayer(e, player2, KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L);
            try {
                shoot(e, player1, KeyEvent.VK_C);
                shoot(e, player2, KeyEvent.VK_N);
            } catch (Exception ex) {

            }
            reload(e, player1, KeyEvent.VK_X);
            reload(e, player2, KeyEvent.VK_M);
        }

        public void keyReleased(KeyEvent e) {

            stopPlayer(e, player1, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D);
            stopPlayer(e, player2, KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L);

            finishReload(e, player1, KeyEvent.VK_X);
            finishReload(e, player2, KeyEvent.VK_M);

        }

        private void movePlayer(KeyEvent e, Player player, int up, int left, int down, int right) {
            if (e.getKeyCode() == up)
                player.setDirY(-1);
            if (e.getKeyCode() == left)
                player.setDirX(-1);
            if (e.getKeyCode() == down)
                player.setDirY(1);
            if (e.getKeyCode() == right)
                player.setDirX(1);
        }

        private void stopPlayer(KeyEvent e, Player player, int up, int left, int down, int right) {
            if (e.getKeyCode() == up)
                player.setDirY(0);
            if (e.getKeyCode() == left)
                player.setDirX(0);
            if (e.getKeyCode() == down)
                player.setDirY(0);
            if (e.getKeyCode() == right)
                player.setDirX(0);
        }

        private void shoot(KeyEvent e, Player player, int shoot) {
            if (e.getKeyCode() == shoot && player.getAmmo() > 0 && !player.getIsReloading()) {
                if (player == player1 && System.currentTimeMillis() - player.getLastShotTime() >= 500) {
                    Bullet bullet = new Bullet(new Point(player.getPos().x + player.getSize() / 2, player.getPos().y + player.getSize() / 2),
                            player.getGunDir().clone(), player.getDamage(), 1, Color.blue);
                    player.setLastShotTime();
                    player.reduceAmmo();
                    Sound.playClip(new File("playerShooting.wav"));
                } else if (player == player2 && System.currentTimeMillis() - player.getLastShotTime() >= 500) {
                    Bullet bullet = new Bullet(new Point(player.getPos().x + player.getSize() / 2, player.getPos().y + player.getSize() / 2),
                            player.getGunDir().clone(), player.getDamage(), 2, Color.red);
                    player.setLastShotTime();
                    player.reduceAmmo();
                    Sound.playClip(new File("playerShooting.wav"));
                }
            }
        }

        private void reload(KeyEvent e, Player player, int reload) {
            if (e.getKeyCode() == reload && !player.getIsReloading()) {
                player.setIsReloading(true);
                player.setTimeReloadStart(System.currentTimeMillis());
                System.out.println(player + " starts a reload.");
            }
        }

        private void finishReload(KeyEvent e, Player player, int reload) {
            if (e.getKeyCode() == reload) {
                player.setIsReloading(false);
                System.out.println(
                        player + " time reload held: " + (System.currentTimeMillis() - player.getTimeReloadStart()));
                System.out.println(player + " reload time: " + player.getReloadTime());
                if (System.currentTimeMillis() - player.getTimeReloadStart() >= player.getReloadTime()) {
                    player.refillAmmo();
					Sound.playClip(new File("playerReload.wav"));
                    System.out.println(player + " reload succeeds. Ammo: " + player.getAmmo());
                }
            }
        }
    }
}