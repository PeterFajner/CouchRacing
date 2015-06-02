public class Vector2
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
