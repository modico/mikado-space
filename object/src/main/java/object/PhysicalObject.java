package object;

import java.awt.Color;
import java.awt.Graphics2D;

import static java.lang.Math.sqrt;

public class PhysicalObject {

    private double mass;
    private final Coordinates coordinates;
    private final double radius;

    public PhysicalObject(double weightKilos,
                          Coordinates coordinates, double radius) {
        this.mass = weightKilos;
        this.coordinates = coordinates;
        this.radius = radius;
    }

    public PhysicalObject absorb(PhysicalObject other) {
        double totalMass = getMass() + other.getMass();
        setXPosition((getXPosition() * mass + other.getXPosition() * other.getMass()) / totalMass);
        setYPosition((getYPosition() * mass + other.getYPosition() * other.getMass()) / totalMass);
        setXVelocity((getXVelocity() * mass + other.getXVelocity() * other.getMass()) / totalMass);
        setYVelocity((getYVelocity() * mass + other.getYVelocity() * other.getMass()) / totalMass);
        setMass(totalMass);
        return this;
    }

    public void setYVelocity(double yVelocity) {
        coordinates.setyVelocity(yVelocity);
    }

    public double getYVelocity() {
        return coordinates.getyVelocity();
    }

    public void setXVelocity(double xVelocity) {
        coordinates.setxVelocity(xVelocity);
    }

    public double getXVelocity() {
        return coordinates.getxVelocity();
    }

    public void setYPosition(double yPosition) {
        coordinates.setyPosition(yPosition);
    }

    public double getYPosition() {
        return coordinates.getyPosition();
    }

    public void setXPosition(double xPosition) {
        coordinates.setxPosition(xPosition);
    }

    public double getXPosition() {
        return coordinates.getxPosition();
    }

    public void hitBy(PhysicalObject other, double backstepIncrement) {
        double timeToCollision = getTimeToCollisionWithOtherObject(other, backstepIncrement);

        // simplify variables
        double m1 = other.getMass();
        double vx1 = other.getXVelocity();
        double vy1 = other.getYVelocity();


        double[] pointOfImpactForThis = {getXPosition() + timeToCollision * getXVelocity(),
                getYPosition() + timeToCollision * getYVelocity()};
        double[] pointOfImpactForOther = {other.getXPosition() + timeToCollision * other.getXVelocity(),
                other.getYPosition() + timeToCollision * other.getYVelocity()};
        double m2 = mass;
        double vx2 = getXVelocity();
        double vy2 = getYVelocity();


        double[] directionOfImpact = {pointOfImpactForThis[0] - pointOfImpactForOther[0], pointOfImpactForThis[1] - pointOfImpactForOther[1]};
        // normalize p12 to length 1
        double distanceBetweenPointsOfImpact = getDistance(directionOfImpact);
        double[] normalizedDirectionOfImpact = {directionOfImpact[0] / distanceBetweenPointsOfImpact, directionOfImpact[1] / distanceBetweenPointsOfImpact};

        // factor in calculation
        double c = normalizedDirectionOfImpact[0] * (vx1 - vx2) + normalizedDirectionOfImpact[1] * (vy1 - vy2);
        // fully elastic
        double e = 1;
        // new speeds
        double[] v1prim = {vx1 - normalizedDirectionOfImpact[0] * (1 + e) * (m2 * c / (m1 + m2)),
                vy1 - normalizedDirectionOfImpact[1] * (1 + e) * (m2 * c / (m1 + m2))};
        double[] v2prim = {vx2 + normalizedDirectionOfImpact[0] * (1 + e) * (m1 * c / (m1 + m2)),
                vy2 + normalizedDirectionOfImpact[1] * (1 + e) * (m1 * c / (m1 + m2))};

        // set variables back
        setXVelocity(v2prim[0]);
        setYVelocity(v2prim[1]);

        other.setXVelocity(v1prim[0]);
        other.setYVelocity(v1prim[1]);

        // step forward to where the objects should be
        setXPosition(getXPosition() + v2prim[0] * (-timeToCollision));
        setYPosition(getYPosition() + v2prim[1] * (-timeToCollision));

        other.setXPosition(other.getXPosition() + v1prim[0] * (-timeToCollision));
        other.setYPosition(other.getYPosition() + v1prim[1] * (-timeToCollision));

    }

    private double getTimeToCollisionWithOtherObject(PhysicalObject other, double backstepIncrement) {
        double timeToCollision = 0;
        double[] vectorFromThisToOther = {getXPosition() - other.getXPosition(), getYPosition() - other.getYPosition()};
        double newDistance = getDistance(vectorFromThisToOther);
        while (newDistance < getRadius() + other.getRadius()) {
            timeToCollision += backstepIncrement;
            vectorFromThisToOther[0] = vectorFromThisToOther[0] + backstepIncrement * (getXVelocity() - other.getXVelocity());
            vectorFromThisToOther[1] = vectorFromThisToOther[1] + backstepIncrement * (getYVelocity() - other.getYVelocity());
            newDistance = getDistance(vectorFromThisToOther);
        }
        return timeToCollision;
    }

    private double getDistance(double[] vector) {
        return sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));
    }

    @Override
    public String toString() {
        return "x=" + getXPosition() + ",y=" + getYPosition() + ",vx=" + getXVelocity() + ",vy=" + getYVelocity() + ",mass="
                + getMass() + ",radius=" + getRadius();
    }

    public void paintPhysicalObject(Graphics2D graphics, Color color, int xPosition, int yPosition, int diameter) {
        graphics.setColor(color);
        graphics.fillOval(xPosition, yPosition, diameter, diameter);
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRadius() {
        return radius;
    }

}