/**
 * aerodynamics calculations for the couch
 */
public class Aerodynamics {
    static void applyAerodynamics(Couch couch)
    {
        // get the angle of attack
        double aoa = couch.angle - couch.velocity.getAngle();
        double angleDelta = aoa - couch.idealAOA; // the difference between the angle of attack and the ideal angle of attack

        // get the speed
        double velocityMagnitude = couch.velocity.getMagnitude();

        // the couch's "up" direction
        double localUp = couch.angle + Math.PI/2;

        // apply lift
        double liftMagnitude = velocityMagnitude * couch.liftCoefficient * Math.cos(angleDelta);
        Vector2 liftForce = new Vector2(localUp, liftMagnitude, true);
        couch.applyForce(liftForce);

        // apply drag
        double dragMagnitude = velocityMagnitude * velocityMagnitude * couch.dragCoefficient * Math.abs(Math.sin(angleDelta)); // drag increases quadratically with speed, deal with it
        Vector2 dragForce = new Vector2(couch.velocity.getAngle() + Math.PI, dragMagnitude, true);
        couch.applyForce(dragForce);

    }
}
