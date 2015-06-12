import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * represents an object in the game world with a position, image, and image scale
 */
public class GameObject
{
    public Point2D pos = new Point2D.Double(0, 0);
    BufferedImage image = null;
    double scale = 1;

    public GameObject(Point2D pos, BufferedImage image, double scale)
    {
        this.pos = pos;
        this.image = image;
        this.scale = scale;
    }

    /**
     * called when the object collides with the couch
     * @param couch the couch
     */
    public void collideWithCouch(Couch couch) {}
}
