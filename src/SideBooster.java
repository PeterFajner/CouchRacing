import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Created by Peter on 2015-06-11.
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
