import java.awt.Color;

public class Speed extends Pickup {
	Speed(Point pos) {
		super(pos);
		color = Color.cyan;
		fadedColor = new Color(0, 255, 255, 100);
	}
}