import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * boosts the couch vertically
 */
public class Booster extends GameObject
{
    public Booster(Point2D pos, BufferedImage image, double scale)
    {
        super(pos, image, scale);
    }

    @Override
    public void collideWithCouch(Couch couch)
    {
        couch.applyForce(new Vector2(0, -50)); // slow the couch's horizontal velocity
    }
}
