import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Cloud extends GameObject
{
	public Cloud(Point2D pos, BufferedImage image)
	{
		super(pos, image);
	}

	@Override
	public void collideWithCouch(Couch couch)
	{
		couch.applyForce(new Vector2(-10 * Math.signum(couch.velocity.x), 0)); // slow the couch's horizontal velocity
	}
}
