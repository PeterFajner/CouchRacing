/**
 * a two-dimensional vector
 */
public class Vector2
{
    public double x;
    public double y;

    /**
     * constructs a vector from x and y components
     * @param x x component
     * @param y y component
     */
    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * contructs a vector from an angle and a magnitude
     * @param angle angle
     * @param magnitude magnitude
     * @param unique this boolean exists only to distinguish this constructor from the x,y one, which also takes two doubles
     */
    public Vector2(double angle, double magnitude, boolean unique)
    {
        this.x = magnitude * Math.cos(angle);
        this.y = magnitude * -Math.sin(angle);
    }

    /**
     * add vector to another
     * @param other the other vector
     */
    public void add(Vector2 other)
    {
        this.x += other.x;
        this.y += other.y;
    }

    /**
     * multiply each component by a scalar
     * @param s the scalar
     */
    public void multiply(double s)
    {
        this.x *= s;
        this.y *= y;
    }

    /**
     * get the angle of the vector
     * @return the vector's angle
     */
    public double getAngle()
    {
        // the game's coordinate grid has a flipped y-axis, so the principal angle must be calculated and then changed based on which quadrant the angle is in
        double referenceAngle = Math.atan2(Math.abs(this.y), Math.abs(this.x));
        double angle = 0;
        if (Math.signum(this.x) < 0) {
            if (Math.signum(this.y) < 0) {
                // quadrant II, flip over y axis
                angle = Math.PI - referenceAngle;
            }
            else {
                // quadrant III, add 180°
                angle = referenceAngle + Math.PI;
            }
        }
        else {
            if (Math.signum(this.y) < 0) {

                // quadrant I
                angle = referenceAngle;
            }
            else {
                // quadrant IV, flip over y
                angle = 2 * Math.PI - referenceAngle;
                //System.out.println("QIV"); // DEBUG
            }
        }
        return angle;
    }

    /**
     * get the magnitude of the vector
     * @return the magnitude of the vector
     */
    public double getMagnitude()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * return the vector as a string
     * @return the vector as a string
     */
    public String toString()
    {
        return "["+x+","+y+"]";
    }
}
