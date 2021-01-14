import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class Pickup {
	Point pos;
	long maxDuration;
	int type;
	Rectangle hitBox;
	Color color;
	Color fadedColor;

	private static ArrayList<Pickup> instances;

	public Pickup(Point pos) {
		this.pos = pos;
		maxDuration = System.currentTimeMillis();
		hitBox = new Rectangle((int) pos.x, (int) pos.y, 20, 20);
	}

	public Point getPos() {
		return pos;
	}

	public Rectangle getHitBox() {
		return hitBox;
	}

	public long getDurationLeft() {
		return System.currentTimeMillis() - maxDuration;
	}

	public void setMaxDuration(long maxDuration) {
		this.maxDuration = maxDuration;
	}

	public static void init() {
		instances = new ArrayList<Pickup>();
	}

	public static ArrayList<Pickup> getInstances() {
		return instances;
	}

	public static void clearPickups() {
		for (int i = 0; i < instances.size(); i++) {
			instances.remove(i);
		}
	}
	
	public Color getColor() {
		return color;
	}
	public Color getFadedColor() {
		return fadedColor;
	}
}