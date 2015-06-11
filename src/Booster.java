import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Created by Peter on 2015-06-11.
 */
public class Booster extends GameObject
{
    public Booster(Point2D pos, BufferedImage image)
    {
        super(pos, image);
    }

    @Override
    public void collideWithCouch(Couch couch)
    {
        couch.applyForce(new Vector2(0, -50)); // slow the couch's horizontal velocity
    }
}
