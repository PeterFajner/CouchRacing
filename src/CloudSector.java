import java.util.ArrayList;

/**
 * Created by Peter on 2015-06-10.
 */
public class CloudSector
{
    public final int x;
    public final int y;
    public ArrayList<Cloud> clouds;

    public CloudSector(int x, int y, ArrayList<Cloud> clouds)
    {
        this.x = x;
        this.y = y;
        this.clouds = clouds;
    }
}
