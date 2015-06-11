import java.util.ArrayList;

/**
 * Created by Peter on 2015-06-11.
 */
public class BoosterSector
{
    public final int x;
    public final int y;
    public ArrayList<Booster> boosters;

    public BoosterSector(int x, int y, ArrayList<Booster> boosters)
    {
        this.x = x;
        this.y = y;
        this.boosters = boosters;
    }
}