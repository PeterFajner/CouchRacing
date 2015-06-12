/** =======================================================================
 * Main Class:    CouchRacing      						Author: Peter Fajner
 * Version:  001                                        Date:  2015-06-01
 *
 * Purpose   Final Project - Couch Racing
 *
 * Course:   Computer Science 202                Teacher:  Ms Jones
 * School:   Sir Winston Churchill High School, Calgary, Alberta, Canada
 * Language: Java SE 8.0    Target Operating System: Java Virtual Machine
 * System:   Undefined running Undefined        IDE: IntelliJ IDEA
 * ========================================================================*/
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
