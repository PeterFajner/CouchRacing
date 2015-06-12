import java.util.ArrayList;

/**
 * represents a collection of GameObjects within a particular 1000x1000 pixel area
 */
public class Sector
{
    public final int x;
    public final int y;
    public ArrayList<GameObject> gameObjects;

    public Sector(int x, int y, ArrayList<GameObject> gameObjects)
    {
        this.x = x;
        this.y = y;
        this.gameObjects = gameObjects;
    }
}
