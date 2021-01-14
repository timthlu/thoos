import java.awt.Color;

public class Damage extends Pickup {
	Damage(Point pos) {
		super(pos);
		color = Color.magenta;
		fadedColor = new Color(255, 0, 255, 100);
	}
}