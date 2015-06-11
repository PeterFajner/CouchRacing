import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Cloud extends GameObject
{
	public Cloud(Point2D pos, BufferedImage image, double scale)
	{
		super(pos, image, scale);
	}

	@Override
	public void collideWithCouch(Couch couch)
	{
		couch.applyForce(new Vector2(-5 * Math.signum(couch.velocity.x), -1 * Math.signum(couch.velocity.y))); // slow the couch's velocity
	}
}
