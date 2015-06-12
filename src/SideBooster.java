import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * this boosts the couch to the right
 */
public class SideBooster extends GameObject
{
    public SideBooster(Point2D pos, BufferedImage image, double scale)
    {
        super(pos, image, scale);
    }

    @Override
    public void collideWithCouch(Couch couch)
    {
        couch.applyForce(new Vector2(50, 0)); // slow the couch's horizontal velocity
    }
}
