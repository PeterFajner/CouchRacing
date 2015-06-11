import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Created by Peter on 2015-06-10.
 */
public class GameObject
{
    public Point2D pos = new Point2D.Double(0, 0);
    BufferedImage image = null;

    public GameObject(Point2D pos, BufferedImage image)
    {
        this.pos = pos;
        this.image = image;
    }

    public void collideWithCouch(Couch couch) {}
}
