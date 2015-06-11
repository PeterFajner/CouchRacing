public class Vector2
{
    public double x;
    public double y;

    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2(double angle, double magnitude, boolean unique)
    {
        this.x = magnitude * Math.cos(angle);
        this.y = magnitude * -Math.sin(angle);
    }

    public void add(Vector2 other)
    {
        this.x += other.x;
        this.y += other.y;
    }

    public double getAngle()
    {
        // each tangent can represent two opposite angles, meaning that we have to find the right one by checking the signs on the x and y values
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

    public double getMagnitude()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public String toString()
    {
        return "["+x+","+y+"]";
    }
}
