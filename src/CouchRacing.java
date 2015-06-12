import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.BufferedImage;

/**
 * Created by Peter on 2015-06-01.
 */
public class CouchRacing {
    public static void main(String[] args)
    {
        Game game = new Game();
    }
}

class Game extends JPanel
{
    JFrame frame;
    Couch couch;
	ArrayList<Sector> environment = new ArrayList<Sector>();
	ArrayList<Sector> activeEnvironment = new ArrayList<Sector>();
	
	final int[] SCREENSIZE = new int[]{1200, 800};
	final int GROUNDLEVEL = 500;
	final double GRAVITYSTRENGTH = 3;
	final int CLOUDFREQ = 1;
	final int BOOSTERFREQ = 2;
	final int SIDEBOOSTERFREQ = 2;
	final double COUCHSCALE = 0.3;
	final double CLOUDSCALE = 0.3;
	final double BOOSTERSCALE = 0.3;
	final double SIDEBOOSTERSCALE = 0.3;
	final double COLLIDERRADIUS = 80; // distance between two objects, in pixels, before they "collide"
	//final String res = "../resources/";

	int height = 0;
	int maxHeight = 0;
	int distance = 0;
	double speed = 0;

	DecimalFormat df = new DecimalFormat("#.##");

	/**
	 * create the window
	 * @param width width of the window in pixels
	 * @param height height of the window in pixels
	 * @return the window
	 */
    JFrame createFrame(int width, int height)
    {
        JFrame frame = new JFrame("Couch Racing");
        frame.add(this);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return frame;
    }

    public Game()
    {
		// create window
        this.frame = createFrame(SCREENSIZE[0], SCREENSIZE[1]);

		// load images
		// while it may be more resilient to load each image in its own try-catch block, this is more compact and realistically you wouldn't want to run the game with an image missing
		BufferedImage couchImage = null;
		BufferedImage cloudImage = null;
		BufferedImage boosterImage = null;
		BufferedImage sideBoosterImage = null;
		try {
			couchImage = ImageIO.read(getClass().getResource("couch.png"));
			cloudImage = ImageIO.read(getClass().getResource("cloud.png"));
			boosterImage = ImageIO.read(getClass().getResource("booster.png"));
			sideBoosterImage = ImageIO.read(getClass().getResource("sidebooster.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// create the couch slightly above the ground at a pi/5 radian angle
		couch = new Couch(new Point2D.Double(0, GROUNDLEVEL - 5), couchImage, COUCHSCALE, Math.PI/5);
		// apply a diagonal slingshot force to the couch
		couch.applyForce(new Vector2(1000, -700)); // apply initial slingshot force

		// setup keyboard input
		KeyListener keyListener = new KeyListenerMotion(0.05, couch);
		this.frame.addKeyListener(keyListener);
		this.frame.setFocusable(true);

		// main loop
		try {
			while (true) {

				// clear list of active objects
				activeEnvironment = new ArrayList<Sector>();

				// check nine object sectors around the couch to see if they exist
				for (double x : new double[]{couch.pos.getX() + 1000, couch.pos.getX() - 1000, couch.pos.getX()}) {
					for (double y : new double[]{couch.pos.getY() + 1000, couch.pos.getY() - 1000, couch.pos.getY()}) {
						// check if sector exists
						boolean sectorIsPopulated = false;
						for (Sector sector : environment) {
							if (sector.x == (int)(x/1000.0) && sector.y == (int)(y/1000.0)) {
								sectorIsPopulated = true;
								activeEnvironment.add(sector);
								environment.add(sector);
								break;
							}
						}
						// if sector doesn't exist, create and populate it
						if (!sectorIsPopulated) {
							ArrayList<GameObject> newObjects = new ArrayList<GameObject>();
							Point2D centre = new Point2D.Double(1000*(int)(x/1000), 1000*(int)(y/1000)); // the centre of the sector; round coords to nearest 1000
							for (int i = 0; i < CLOUDFREQ; i++) {
								Cloud cloud = new Cloud(randomCoords(centre, 500), cloudImage, CLOUDSCALE);
								newObjects.add(cloud);
							}
							for (int i = 0; i < BOOSTERFREQ; i++) {
								Booster booster = new Booster(randomCoords(centre, 500), boosterImage, CLOUDSCALE);
								newObjects.add(booster);
							}
							for (int i = 0; i < SIDEBOOSTERFREQ; i++) {
								SideBooster sideBooster = new SideBooster(randomCoords(centre, 500), sideBoosterImage, CLOUDSCALE);
								newObjects.add(sideBooster);
							}
							Sector newSector = new Sector((int)(x/1000), (int)(y/1000), newObjects);
							activeEnvironment.add(newSector);
							environment.add(newSector);
						}
					}
				}

				// apply gravity
				couch.applyForce(new Vector2(0, GRAVITYSTRENGTH));

				// check for and handle collisions
				GameObject collided = getFirstCollison(couch, activeEnvironment);
				if (collided != null) {
					collided.collideWithCouch(couch); // pass the collision on to the object
				}

				// apply aerodynamics (lift and drag)
				Aerodynamics.applyAerodynamics(couch);

				// update couch velocity and position
				couch.update();

				// update values in the display labels
				this.height = (int)(GROUNDLEVEL - couch.pos.getY());
				if (this.height > this.maxHeight) {
					this.maxHeight = this.height;
				}
				this.distance = (int)couch.pos.getX();
				this.speed = couch.velocity.getMagnitude();

				// draw to screen
				this.repaint();

				// check if couch hit the ground
				if (couch.pos.getY() > GROUNDLEVEL) {
					break;
				}

				// sleep until next frame
				// this isn't a very good way to handle FPS, but it works okay
				Thread.sleep(10);
			}

			// ensure that the speed gauge ends at zero
			speed = 0;
			this.repaint();
		}
		// prevents an exception when the window is closed
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
    }

	/**
	 * generate a coordinate pair within a cetrain radius
	 * @param centre the centre of the distribution
	 * @param radius the radius of the distribution
	 * @return the random coordinates
	 */
	Point2D randomCoords(Point2D centre, int radius)
	{
		Random r = new Random();
		int x = r.nextInt(2*radius) - radius + (int)centre.getX();
		int y = r.nextInt(2*radius) - radius + (int)centre.getY();
		return new Point2D.Double(x, y);
	}

	/**
	 * gets the first collision between the couch and an environment object
	 * @param couch the couch
	 * @param sectors a list of active sectors to search for objects
	 * @return the object collided with, or null if no collision
	 */
	GameObject getFirstCollison(Couch couch, ArrayList<Sector> sectors)
	{
		for (Sector sector : sectors) {
			for (GameObject obj : sector.gameObjects) {
				Point2D centrePos = new Point2D.Double(obj.pos.getX() + obj.image.getWidth() * obj.scale / 2, obj.pos.getY() + obj.image.getHeight() * obj.scale / 2);
				if (couch.pos.distanceSq(centrePos) < Math.pow(COLLIDERRADIUS, 2)) {
					//System.out.println("Collided with " + obj); // DEBUG
					return obj;
				}
			}
		}
		return null;
	}

	/**
	 * draws the objects onto the screen
	 * @param g the awt graphics library object
	 */
	@Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
				
		// render couch in middle of screen
		int xPos = frame.getWidth()/2;
		int yPos = frame.getHeight()/2;
		try {
			// rotate the couch according to its angle
			// note that transformations are in reversed order
			AffineTransform xform = new AffineTransform();
			xform.translate(frame.getWidth()/2, frame.getHeight()/2); // move couch to centre of window
			xform.rotate(-couch.angle); // do the rotation; default is clockwise, we want counterclockwise
			xform.scale(couch.scale, couch.scale);
			xform.translate(-couch.image.getWidth() / 2, -couch.image.getHeight() / 2); // move to centre of couch so it rotates around its centre

			// draw the couch
			g2d.drawImage(couch.image, xform, null);
		} catch (NullPointerException e) {
			System.out.println("Couch image not found!");
			g2d.fillRect(xPos, yPos, 30, 30); // draw a square instead of the couch
		}

		// render active environment
		// all objects are rendered as offsets of the couch's position, as it is always in the middle of the screen
		try {
			for (Sector sector : activeEnvironment) {
				for (GameObject obj : sector.gameObjects) {
					int objxPos = (int) (frame.getWidth() / 2 + obj.pos.getX() - couch.pos.getX());
					int objyPos = (int) (frame.getHeight() / 2 + obj.pos.getY() - couch.pos.getY());
					int objWidth = (int) (obj.image.getWidth(null) * obj.scale);
					int objHeight = (int) (obj.image.getHeight(null) * obj.scale);
					try {
						g2d.drawImage(obj.image, objxPos, objyPos, objWidth, objHeight, null);
					} catch (NullPointerException e) {
						g2d.fillOval(objxPos, objyPos, 40, 20); // draw an oval instead of the object
					}
				}
			}
		} catch (ConcurrentModificationException e) {
			// the renderer doesn't modify anything, so it should be alright to silence this
		}

		// render ground
		try {
			int groundyPos = (int)(frame.getHeight()/2 + GROUNDLEVEL - couch.pos.getY() + couch.image.getHeight()/2 * couch.scale);
			g2d.setColor(new Color(0.3372549f, 0.5019608f, 0.1764706f));
			g2d.fillRect(0, groundyPos, frame.getBounds().width, 1000);
		} catch (NullPointerException e) {
			// this just means the couch hasn't loaded yet
		}

		// render the display labels
		g2d.drawString("Distance: "+distance/100, this.frame.getBounds().width - 150, 20);
		g2d.drawString("Height: "+height/100, this.frame.getBounds().width - 150, 40);
		g2d.drawString("Max Height: "+maxHeight/100, this.frame.getBounds().width - 150, 60);
		g2d.drawString("Speed: "+df.format(speed), this.frame.getBounds().width - 150, 80);

		//System.out.println("Round done"); // DEBUG
    }
}

/**
 * gets keyboard input for rotating the couch
 */
class KeyListenerMotion extends KeyAdapter
{
	private double deltaAngle;
	private Couch couch;

	public KeyListenerMotion(double deltaAngle, Couch couch)
	{
		this.deltaAngle = deltaAngle;
		this.couch = couch;
	}

	public void rotate(double deltaAngle) {
		couch.rotate(deltaAngle);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.rotate(this.deltaAngle);
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.rotate(-this.deltaAngle);
		}
	}

}

/**
TODO

 X rotation via keyboard
 X collision checking
 X air resistance / aerodynamics
 X cloud push down
 X procedural clouds
 X procedural ground
 X air draft push up
 X fix collision
 X create unified sector class
 X cloud texture
 X better ground texture
 - points
 - FPS selector
 - coins to grab
 X make things constants
 adjust FPS
 X starting elastic firing
 X max distance
 X max height
 planning evidence
 X reorganize
 comment
 adjust aerodynamicity
 X change ground colour
 adjust hitboxes
 background?
 set final speed to 0
*/
