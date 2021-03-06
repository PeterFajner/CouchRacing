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
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Cloud extends GameObject
{
	public Cloud(Point2D pos, BufferedImage image, double scale)
	{
		super(pos, image, scale);
	}

	@Override
	public void collideWithCouch(Couch couch)
	{
		couch.applyForce(new Vector2(-5 * Math.signum(couch.velocity.x), -1 * Math.signum(couch.velocity.y))); // slow the couch's velocity
	}
}
