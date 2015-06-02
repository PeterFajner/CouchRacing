import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

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

class Vector2
{
    public double x;
    public double y;

    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2 other)
    {
        this.x += other.x;
        this.y += other.y;
    }

    public String toString()
    {
        return "["+x+","+y+"]";
    }
}

class Couch
{
    public Point2D pos;
    public double angle; // angle counterclockwise from right horizontal, in radians
    public double mass;
    Vector2 totalForce;
    Vector2 velocity;

    public Couch()
    {
        this.pos = new Point2D.Double(10, 10);
        this.angle = 0;
        this.totalForce = new Vector2(0, 0);
        this.velocity = new Vector2(0,0);
        this.mass = 0.00001;
    }

    public void applyForce(Vector2 force)
    {
        this.totalForce.add(force);
    }

    public void update()
    {
        // update velocity
        // multiply the accumulated force by the mass, and add it to the velocity
        Vector2 addedVelocity = Vector2Tools.multiply(this.totalForce, this.mass);
        this.velocity = Vector2Tools.add(this.velocity, addedVelocity);

        // update position
        this.pos.setLocation(this.velocity.x + this.pos.getX(), this.velocity.y + this.pos.getY());
    }

}

class Game extends JPanel
{
    JFrame frame;
    Couch couch;

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
        this.frame = createFrame(1200, 800);
        couch = new Couch();
    }

    public void gameLoop() {
        applyInitialForce();
        try {
            while (true) {
                this.update();
                this.repaint();
                Thread.sleep(10);
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    void applyInitialForce()
    {
        couch.applyForce(new Vector2(10, -10));
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillOval(frame.getWidth()/2, frame.getHeight()/2, 30, 30);
        // for object in objectList, render object with offset to couch
    }

    public void update()
    {
        couch.applyForce(new Vector2(0, 1));
        couch.update();
        System.out.println(couch.pos);
    }
}