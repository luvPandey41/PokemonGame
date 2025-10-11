package object;

import java.io.IOException;

import javax.imageio.ImageIO;

public class OBJ_Bike extends SuperObject{
	public OBJ_Bike() {
		name = "Bike";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/bike.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		description = "[" + name + "]A trusty bicycle.";
		collision = true;
	}
}