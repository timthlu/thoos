/*
*/

import java.awt.Rectangle;
import java.awt.Color;
import java.util.Calendar;
import java.util.GregorianCalendar;

class Player extends MapObject {
	
	private double speed;
	private Point gunDir;
	private int health;
	private int maxHealth;
	private int lives;
	private Point lastPos;
	
	private int damage;
	private int maxAmmo;
	private int ammo;
	private long timeReloadStart;
	private long reloadTime; // in milliseconds
	private boolean isReloading;
	private long timeRespawnStart;
	private long respawnTime;
	private boolean isRespawning;
	private long lastShotTime; //for firing rate
	
	private long speedTime;
	private long damageTime;
	
	Player(Point pos, Point gunDir, Color color) {
		super(pos, new Point(0, 0), 40, color);
		this.gunDir = gunDir;
		speed = 3;
		maxHealth = 50;
		health = maxHealth;
		lives = 3;
		damage = 10;
		maxAmmo = 8;
		ammo = maxAmmo;
		reloadTime = 1500;
		isRespawning = false;
		lastPos = new Point(pos.x, pos.y);
		speedTime = 0;
		damageTime = 0;
		lastShotTime = 0;
		
		hitBox = new Rectangle((int) pos.x, (int) pos.y, size, size);
	}
	
	//METHODS
	//Player Position, Direction Methods
	public void setDirX(double x) {
		dir.x = x;
	}
	
	public void setDirY(double y) {
		dir.y = y;
	}

	public Point getGunDir() {
		return gunDir;
	}
	
	public void setGunDir(Point gunDir) {
		this.gunDir = gunDir;
	}
	
	public void move(double elapsedTime) {
		if (dir.x != 0 || dir.y != 0){
			gunDir.x = -dir.x;
			gunDir.y = -dir.y;
		}

		// note: with this move method, player moves faster diagonally
		pos = new Point(pos.x + dir.x * speed * elapsedTime * 100, pos.y + dir.y * speed * elapsedTime * 100);
		//move hit box
		hitBox = new Rectangle((int) pos.x, (int) pos.y, size, size);
		//System.out.println(elapsedTime);
	}
	
	//Player Ammo and Reload Methods
	
	public int getAmmo() {
		return ammo;
	}
	
	public void reduceAmmo() {
		--ammo;
	}
	
	public void refillAmmo	() {
		ammo = maxAmmo;
	}
	
	public boolean getIsReloading() {
		return isReloading;
	}
	
	public void setIsReloading(boolean isReloading) {
		this.isReloading = isReloading;
	}
	
	public long getReloadTime() {
		return reloadTime;
	}
	
	public long getTimeReloadStart() {
		return timeReloadStart;
	}
	
	public void setTimeReloadStart(long timeReloadStart) {
		this.timeReloadStart = timeReloadStart;
	}
	
	//Player Respawning Methods
	
	public boolean getIsRespawning() {
		return isRespawning;
	}
	
	public void setIsRespawning(boolean isRespawning) {
		this.isRespawning = isRespawning;
	}
	
	public long getRespawnTime() {
		return respawnTime;
	}
	
	public long getTimeRespawnStart() {
		return timeRespawnStart;
	}
	
	public void setTimeRespawnStart(long timeRespawnStart) {
		this.timeRespawnStart = timeRespawnStart;
	}
	
	//Player Health and Lives Methods
	
	public int getHealth() {
		return health;
	}
	
	public void reduceHealth(int health) {
		this.health -= health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public int getLives() {
		return lives;
	}
	
	public void loseLife() {
		lives--;
	}
	
	//Player Collision - lastPos Methods
	
	public Point getLastPos() {
		return lastPos;
	}
	
	public void setLastPos(Point lastPos) {
		this.lastPos.x = lastPos.x;
		this.lastPos.y = lastPos.y;
	}
	
	//Player Pickup Methods
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public long getSpeedTime() {
		return speedTime;
	}
	
	public void startSpeedPickup() {
		if (speedTime == 0) {
			speed = (speed*1.5);
		}
		speedTime = System.currentTimeMillis();
	}
	
	public void endSpeedPickup() {
		speedTime = 0;
		speed = (speed/1.5);
	}
	
	public long getDamageTime() {
		return damageTime;
	}
	
	public void startDamagePickup() {
		if (damageTime == 0) {
			damage = damage*2;
		}
		damageTime = System.currentTimeMillis();
	}
	
	public void endDamagePickup() {
		damageTime = 0;
		damage = damage/2;
	}
	
	public void useHealthPickup() {
		if (health + 20 > maxHealth) {
			health = maxHealth;
		} else {
			health = health + 20;
		}
	}
	
	public void clearBuffs() {
		if (speedTime != 0) {
			speedTime = 0;
			speed = (speed/1.5);
		}
		if (damageTime != 0) {
			damageTime = 0;
			damage = damage/2;
		}
	}
	
	//Firing rate methods
	public long getLastShotTime() {
		return lastShotTime;
	}
	public void setLastShotTime() {
		lastShotTime = System.currentTimeMillis();
	}
}

 
