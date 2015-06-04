import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Cloud
{
	public Point2D pos;
	BufferedImage image;

	public Cloud(Point2D pos, BufferedImage image)
	{
		this.pos = pos;
		this.image = image;
	}
}
