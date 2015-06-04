import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.awt.image.BufferedImage;

public class Couch
{
    public Point2D pos;
    public double angle; // angle counterclockwise from right horizontal, in radians
    public double mass;
    Vector2 totalForce;
    Vector2 velocity;
	BufferedImage image = null;

    public Couch(Point2D pos)
    {
        this.pos = pos;
        this.angle = 0;
        this.totalForce = new Vector2(0, 0);
        this.velocity = new Vector2(0,0);
        this.mass = 100;
    }
	
	public Couch()
	{
		this(new Point2D.Double(10, 10));
	}
	
	public void setImage(BufferedImage image)
	{
		this.image = image;
	}

    public void applyForce(Vector2 force)
    {
        this.totalForce.add(force);
    }

    public void update()
    {
        // update velocity
        // multiply the accumulated force by the mass, and add it to the velocity
        Vector2 addedVelocity = Vector2Tools.multiply(this.totalForce, 1/this.mass);
        this.velocity = Vector2Tools.add(this.velocity, addedVelocity);
		this.totalForce = new Vector2(0, 0);

        // update position
        this.pos.setLocation(this.velocity.x + this.pos.getX(), this.velocity.y + this.pos.getY());
    }

}
