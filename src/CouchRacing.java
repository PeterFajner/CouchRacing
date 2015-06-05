import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
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
	
	final int[] SCREENSIZE = new int[]{1200, 800};
	final int GROUNDLEVEL = 500;
	final String res = "../resources/";
	
	ArrayList<Cloud> clouds = new ArrayList<Cloud>();

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
        couch = new Couch(new Point2D.Double(0, 0)); // create the couch
		
		// set an image for the couch
		BufferedImage couchImage;
		try {
			//couchImage = ImageIO.read(new File("../res/leathersofa.png"));
            couchImage = ImageIO.read(getClass().getResource("leathersofa.png"));
			couch.setImage(couchImage);
			//System.out.println(couch.image);
		} catch (IOException e) {
			//System.out.println("How can the couch be real if its image isn't real?");
            e.printStackTrace();
		}
    }
	
	public Cloud createCloud(int[] xSpread, int[] ySpread, BufferedImage image)
	{
		Random r = new Random();
		int x = r.nextInt(xSpread[1] - xSpread[0]) + xSpread[0];
		int y = r.nextInt(ySpread[1] - ySpread[0]) + ySpread[0];
		return new Cloud(new Point2D.Double(x, y), image);
	}
	
    public void gameLoop() {
        couch.applyForce(new Vector2(100, -500)); // apply initial slingshot force; initially hardcoded
		// create some clouds
		BufferedImage cloudImage = null;
		try {
			cloudImage = ImageIO.read(getClass().getResource("clouds3.png"));
		} catch (IOException e) {
			//System.out.println("How can clouds be real if their textures aren't real?");
			e.printStackTrace();
		}
		for (int i = 0; i < 25; i++) {
			Cloud cloud = createCloud(new int[]{-3000, 3000}, new int[]{-1000, 1000}, cloudImage);
			clouds.add(cloud);
		}
		
		// main loop
        try {
            while (true) {
                couch.applyForce(new Vector2(0, 3)); // apply gravity
				couch.update();
				System.out.println(couch.pos);
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

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
				
		// render couch in middle of screen
		int xPos = frame.getWidth()/2;
		int yPos = frame.getHeight()/2;
		double scale = 0.1;

		//System.out.println(couch.image);

		int width = 0;
		int height = 0;

		try {
			width = (int) (couch.image.getWidth(null) * scale);
			height = (int) (couch.image.getHeight(null) * scale);
			g2d.drawImage(couch.image, xPos, yPos, width, height, null);
		} catch (NullPointerException e) {
			System.out.println("Couch image not found!");
			g2d.fillRect(xPos, yPos, 30, 30);
		}

		System.out.println("Round done");

		for (Cloud cloud : clouds) {
			int cloudxPos = (int)(frame.getWidth()/2 + cloud.pos.getX() - couch.pos.getX());
			int cloudyPos = (int)(frame.getHeight()/2 + cloud.pos.getY() - couch.pos.getY());
			double cloudScale = 0.3;
			int cloudWidth = (int) (cloud.image.getWidth(null) * cloudScale);
			int cloudHeight = (int) (cloud.image.getHeight(null) * cloudScale);
			try {
				g2d.drawImage(cloud.image, cloudxPos, cloudyPos, cloudWidth, cloudHeight, null);
			} catch (NullPointerException e) {
				g2d.fillOval(cloudxPos, cloudyPos, 40, 20);
			}
		}

		/*for (Cloud cloud : clouds) {
			//g2d.fillOval((int)(frame.getWidth()/2 + cloud.pos.getX() - couch.pos.getX()), (int)(frame.getHeight()/2 + cloud.pos.getY() - couch.pos.getY()), 40, 20);
			int cloudxPos = (int)(frame.getWidth()/2 + cloud.pos.getX() - couch.pos.getX());
			int cloudyPos = (int)(frame.getHeight()/2 + cloud.pos.getY() - couch.pos.getY());
			g2d.drawImage(cloud.image, cloudxPos, cloudyPos, null);
		}*/
        // for object in objectList, render object with offset to couch
    }
}

/**
TODO

 rotation via keyboard
collision checking
air resistance
cloud push down
air draft push up
cloud texture
ground texture
points
FPS selector
 procedural cloud generation
 coins to grab
*/
