import java.util.ArrayList;

/**
 * Created by Peter on 2015-06-11.
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
