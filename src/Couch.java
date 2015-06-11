import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.awt.image.BufferedImage;

public class Couch extends GameObject
{
    public double angle; // angle counterclockwise from right horizontal, in radians
    public double mass;
    public double liftCoefficient;
    public double dragCoefficient;
    public double idealAOA; // ideal angle of attack
    Vector2 totalForce;
    Vector2 velocity;

    public Couch(Point2D pos, BufferedImage image)
    {
        super(pos, image);
        this.angle = 0;
        this.totalForce = new Vector2(0, 0);
        this.velocity = new Vector2(0,0);
        this.mass = 100;
        this.liftCoefficient = 1;
        this.dragCoefficient = 0.3;
        this.idealAOA = 0.01; // a little under 6°, couches aren't aerodynamic
    }
	
	public void setImage(BufferedImage image)
	{
		this.image = image;
	}

    public void applyForce(Vector2 force)
    {
        this.totalForce.add(force);
    }

    /**
     *
     * @param angle the angle to rotate by in radians
     * @return couch angle
     */
    public double rotate(double angle)
    {
        this.angle += angle;
        return this.angle;
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
