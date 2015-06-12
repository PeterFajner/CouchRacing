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
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.awt.image.BufferedImage;

/**
 * the couch, a GameObject controlled by the player
 */
public class Couch extends GameObject
{
    public double angle; // angle counterclockwise from right horizontal, in radians
    public double mass;
    public double liftCoefficient;
    public double dragCoefficient;
    public double idealAOA; // ideal angle of attack
    Vector2 totalForce; // cumulative force applied on the couch between updates
    Vector2 velocity;

    public Couch(Point2D pos, BufferedImage image, double scale, double angle)
    {
        super(pos, image, scale);
        this.angle = angle;
        this.totalForce = new Vector2(0,0);
        this.velocity = new Vector2(0,0);
        this.mass = 80;
        this.liftCoefficient = 1;
        this.dragCoefficient = 0.2;
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
     * rotate the couch by a certain angle
     * @param angle the angle in radians
     * @return couch angle
     */
    public double rotate(double angle)
    {
        this.angle += angle;
        return this.angle;
    }

    /**
     * convert the total force applied onto the couch into velocity, and then move the couch
     */
    public void update()
    {
        // divide the accumulated force by the mass, and add it to the velocity
        Vector2 addedVelocity = Vector2Tools.multiply(this.totalForce, 1.0/this.mass);
        this.velocity.add(addedVelocity);
		this.totalForce = new Vector2(0, 0); // clear the accumulated force

        // update position
        this.pos.setLocation(this.velocity.x + this.pos.getX(), this.velocity.y + this.pos.getY());
    }

}
