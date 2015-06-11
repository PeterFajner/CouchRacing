import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;
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
        game.gameLoop();
    }
}

class Game extends JPanel
{
    JFrame frame;
    Couch couch;
	//ArrayList<CloudSector> clouds = new ArrayList<CloudSector>();
	//ArrayList<CloudSector> renderedClouds = new ArrayList<CloudSector>();
	//ArrayList<BoosterSector> boosters = new ArrayList<BoosterSector>();
	//ArrayList<BoosterSector> renderedBoosters = new ArrayList<BoosterSector>();
	ArrayList<Sector> environment = new ArrayList<Sector>();
	ArrayList<Sector> activeEnvironment = new ArrayList<Sector>();
	
	final int[] SCREENSIZE = new int[]{1200, 800};
	final int GROUNDLEVEL = 500;
	final String res = "../resources/";

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
        this.frame = createFrame(SCREENSIZE[0], SCREENSIZE[1]);



		// set an image for the couch
		BufferedImage couchImage = null;
		try {
			//couchImage = ImageIO.read(new File("../res/leathersofa.png"));
            couchImage = ImageIO.read(getClass().getResource("couch.png"));
			//System.out.println(couch.image);
		} catch (IOException e) {
			//System.out.println("How can the couch be real if its image isn't real?");
            e.printStackTrace();
		}
		couch = new Couch(new Point2D.Double(0, 0), couchImage); // create the couch

		// setup keyboard input
		KeyListener keyListener = new KeyListenerMotion(0.05, couch);
		this.frame.addKeyListener(keyListener);
		this.frame.setFocusable(true);
    }
	
	public Cloud createCloud(Point2D centre, int radius, BufferedImage image)
	{
		Random r = new Random();
		int x = r.nextInt(2*radius) - radius + (int)centre.getX();
		int y = r.nextInt(2*radius) - radius + (int)centre.getY();
		return new Cloud(new Point2D.Double(x, y), image);
	}

	public Booster createBooster(Point2D centre, int radius, BufferedImage image)
	{
		Random r = new Random();
		int x = r.nextInt(2*radius) - radius + (int)centre.getX();
		int y = r.nextInt(2*radius) - radius + (int)centre.getY();
		return new Booster(new Point2D.Double(x, y), image);
	}
	
    public void gameLoop() {

        couch.applyForce(new Vector2(100, -500)); // apply initial slingshot force; initially hardcoded

		// load cloud image
		BufferedImage cloudImage = null;
		try {
			cloudImage = ImageIO.read(getClass().getResource("cloud.png"));
		} catch (IOException e) {
			//System.out.println("How can clouds be real if their textures aren't real?");
			e.printStackTrace();
		}

		// load booster image
		BufferedImage boosterImage = null;
		try {
			boosterImage = ImageIO.read(getClass().getResource("booster.png"));
		} catch (IOException e) {
			//System.out.println("How can clouds be real if their textures aren't real?");
			e.printStackTrace();
		}
		
		// main loop
        try {
            while (true) {

				// clear list of active objects
				activeEnvironment = new ArrayList<Sector>();
				// check nine sectors around couch to see if need to generate more clouds
				// additionally, assign the nearby sectors to be rendered
				for (double x : new double[]{couch.pos.getX() + 1000, couch.pos.getX() - 1000, couch.pos.getX()}) {
					for (double y : new double[]{couch.pos.getY() + 1000, couch.pos.getY() - 1000, couch.pos.getY()}) {
						// check sector
						boolean sectorIsPopulated = false;
						for (Sector sector : environment) {
							if (sector.x == (int)(x/1000.0) && sector.y == (int)(y/1000.0)) {
								sectorIsPopulated = true;
								activeEnvironment.add(sector);
								environment.add(sector);
								break;
							}
						}
						if (!sectorIsPopulated) {
							// this executes when the nearby sector does not have clouds
							ArrayList<GameObject> newObjects = new ArrayList<GameObject>();
							for (int i = 0; i < 2; i++) {
								Cloud cloud = createCloud(new Point2D.Double(1000*(int)(x/1000), 1000*(int)(y/1000)), 500, cloudImage); // round coords to nearest 1000
								newObjects.add(cloud);
							}
							for (int i = 0; i < 2; i++) {
								Booster booster = createBooster(new Point2D.Double(1000 * (int) (x / 1000), 1000 * (int) (y / 1000)), 500, boosterImage); // round coords to nearest 1000
								newObjects.add(booster);
							}
							Sector newSector = new Sector((int)(x/1000), (int)(y/1000), newObjects);
							activeEnvironment.add(newSector);
							environment.add(newSector);
						}
					}
				}


                couch.applyForce(new Vector2(0, 3)); // apply gravity

				// check for collisions
				GameObject collided = getFirstCollison(couch, activeEnvironment);
				if (collided != null) {
					collided.collideWithCouch(couch); // pass the collision on to the object
				}

				// apply aerodynamics (lift and drag)
				Aerodynamics.applyAerodynamics(couch);

				couch.update();
				// System.out.println(couch.pos); // DEBUG
                this.repaint();
                Thread.sleep(10);

				// check if couch hit the ground
				if (couch.pos.getY() > GROUNDLEVEL) {
					break;
				}
            }
        }
		// why do I need to do this
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

	// gets the first collision with the couch and something else
	GameObject getFirstCollison(Couch couch, ArrayList<Sector> sectors)
	{
		for (Sector sector : sectors) {
			for (GameObject obj : sector.gameObjects) {
				if (couch.pos.distanceSq(obj.pos) < Math.pow(50, 2)) {
					System.out.println("Collided with " + obj); // DEBUG
					return obj;
				}
			}
		}
		return null;
	}

	@Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
				
		// render couch in middle of screen
		int xPos = frame.getWidth()/2;
		int yPos = frame.getHeight()/2;
		double scale = 0.3;
		int width = 0;
		int height = 0;
		try {
			//width = (int) (couch.image.getWidth(null) * scale);
			//height = (int) (couch.image.getHeight(null) * scale);

			// rotate the image
			// https://stackoverflow.com/questions/4918482/rotating-bufferedimage-instances
			// note that transformations are in reversed order
			AffineTransform xform = new AffineTransform();
			xform.translate(couch.image.getWidth() / 2, couch.image.getHeight() / 2); // move transform to centre of component
			xform.rotate(-couch.angle); // do the rotation; default is clockwise, we want counterclockwise
			xform.scale(scale, scale);
			xform.translate(-couch.image.getWidth() / 2, -couch.image.getHeight() / 2); // move to centre of object so it rotates around its centre

			g2d.drawImage(couch.image, xform, null);

			//g2d.drawImage(couch.image, xPos, yPos, width, height, null);
		} catch (NullPointerException e) {
			System.out.println("Couch image not found!");
			g2d.fillRect(xPos, yPos, 30, 30);
		}


		// render active environment
		try {
			for (Sector sector : activeEnvironment) {
				for (GameObject obj : sector.gameObjects) {
					int cloudxPos = (int) (frame.getWidth() / 2 + obj.pos.getX() - couch.pos.getX());
					int cloudyPos = (int) (frame.getHeight() / 2 + obj.pos.getY() - couch.pos.getY());
					double cloudScale = 0.3;
					int cloudWidth = (int) (obj.image.getWidth(null) * cloudScale);
					int cloudHeight = (int) (obj.image.getHeight(null) * cloudScale);
					try {
						g2d.drawImage(obj.image, cloudxPos, cloudyPos, cloudWidth, cloudHeight, null);
					} catch (NullPointerException e) {
						g2d.fillOval(cloudxPos, cloudyPos, 40, 20);
					}
				}
			}
		} catch (ConcurrentModificationException e) {
			// the renderer doesn't actually modify anything, so it should be alright
		}

		// render ground
		try {
			int groundyPos = (int) (frame.getHeight() / 2 + GROUNDLEVEL - couch.pos.getY());
			g2d.setColor(Color.GREEN);
			g2d.fillRect(0, groundyPos, frame.getBounds().width, 1000);
		} catch (NullPointerException e) {
			// this just means the couch hasn't loaded yet
		}

		//System.out.println("Round done"); // DEBUG
    }
}

// gets keyboard input for rotation and rotates the couch
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
air draft push up
 fix collision
 create unified sector class
X cloud texture
ground texture
points
FPS selector
 - coins to grab
 make things constants
 adjust FPS
 starting elastic firing
 max distance
 max height
 planning evidence
 reorganize
 comment
 change ground colour
*/
