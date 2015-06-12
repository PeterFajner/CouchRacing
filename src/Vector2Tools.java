
/**
 * methods for working with Vector2 objects
 * many of these are identical to those in the Vector2 class but return a new Vector2 rather than operating on an existing one
 */
public class Vector2Tools {

    static Vector2 multiply(Vector2 a, double b)
    {
        return new Vector2(a.x * b, a.y * b);
    }

    static Vector2 add(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    // returns an angle
    // this works with an inverted y axis
    static double getAngle(Vector2 a)
    {
        return a.getAngle();
    }


}