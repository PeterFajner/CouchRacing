import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

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
        couch = new Couch(new Point2D.Double(0, 0));
    }
	
	public Cloud createCloud(int[] xSpread, int[] ySpread)
	{
		Random r = new Random();
		int x = r.nextInt(xSpread[1] - xSpread[0]) + xSpread[0];
		int y = r.nextInt(ySpread[1] - ySpread[0]) + ySpread[0];
		return new Cloud(new Point2D.Double(x, y));
	}
	
    public void gameLoop() {
        couch.applyForce(new Vector2(100, -500)); // apply initial slingshot force; initially hardcoded
		// create some clouds
		for (int i = 0; i < 250; i++) {
			Cloud cloud = createCloud(new int[]{-3000, 3000}, new int[]{-1000, 1000});
			clouds.add(cloud);
		}
        try {
            while (true) {
                couch.applyForce(new Vector2(0, 3)); // gravity
				couch.update();
				System.out.println(couch.pos);
                this.repaint();
                Thread.sleep(10);
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
        g2d.fillRect(frame.getWidth()/2, frame.getHeight()/2, 30, 30); // render couch in middle of screen
		for (Cloud cloud : clouds) {
			g2d.fillOval((int)(frame.getWidth()/2 + cloud.pos.getX() - couch.pos.getX()), (int)(frame.getHeight()/2 + cloud.pos.getY() - couch.pos.getY()), 40, 20);
		}
        // for object in objectList, render object with offset to couch
    }
}
