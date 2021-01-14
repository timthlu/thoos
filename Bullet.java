
/*
*/

import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Color;

class Bullet extends MapObject {

	private int speed;
	private int damage;
	private static ArrayList<Bullet> instances;
	private int playerShot; // 1 or 2 (1 meaning player 1 shot this bullet, 2
							// meaning player 2 shot this bullet)

	Bullet(Point pos, Point dir, int damage, int playerShot, Color color) {

		super(pos, dir, 10, color);
		this.damage = damage;
		this.speed = 15;
		this.playerShot = playerShot;
		instances.add(this);
		// System.out.println("Bullet created at " + pos);
		// System.out.println("Number of bullets: " + instances.size());

		hitBox = new Rectangle((int) getPos().x - 5, (int) getPos().y - 5, 10, 10);

	}

	// TODO: add timeout and collision to delete extra bullets; be careful of
	// null in instances

	public static void init() {
		instances = new ArrayList<Bullet>();
	}

	public static ArrayList<Bullet> getInstances() {
		return instances;
	}

	public void setDirX(int x) {
		System.out.println("Bullet setDirX: " + x);
		this.dir.x = x;
	}

	public void setDirY(int y) {
		System.out.println("Bullet setDirY: " + y);
		this.dir.y = y;
	}

	public Point getPos() {
		return pos;
	}

	public int getSize() {
		return size;
	}

	public int getDamage() {
		return this.damage;
	}

	public void move(double elapsedTime) {
		//System.out.println(this.dir.x + " " + speed + " " + elapsedTime + " ");
		pos = new Point(pos.x + dir.x * speed * elapsedTime * 100, pos.y + dir.y * speed * elapsedTime * 100);
		hitBox = new Rectangle((int) pos.x, (int) pos.y, size, size);
	}

	// for collisions
	public int getPlayerShot() {
		return playerShot;
	}

	public void setPlayerShot(int playerShot) {
		this.playerShot = playerShot;
	}
}
