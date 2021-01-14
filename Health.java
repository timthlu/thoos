import java.awt.Color;

public class Health extends Pickup {
	Health(Point pos) {
		super(pos);
		color = Color.green;
		fadedColor = new Color(0, 255, 0, 100);
	}
}