package space;

import static java.lang.Math.sqrt;

import object.Coordinates;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import object.PhysicalObject;

public class BouncingBallsTest {

    @Test
    public void straightOn() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(0, 0, 0, 0), 1);
        PhysicalObject two = new PhysicalObject(1, new Coordinates(-1, 0, 1, 0), 1);
        one.hitBy(two, -Space.iterationTimeInSeconds / 10);
        assertEquals(1.0, one.getXVelocity(), 0.0001);
        assertEquals(0.0, one.getYVelocity(), 0.0001);
        assertEquals(0.0, two.getXVelocity(), 0.0001);
        assertEquals(0.0, two.getYVelocity(), 0.0001);
        assertTrue(-2.0 < two.getXPosition());
        assertEquals(1.0, one.getXPosition(), 0.0001);
    }

    @Test
    public void straightOnVerticalDifferentMass() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(0, 0, 0, -1), 0.5);
        PhysicalObject two = new PhysicalObject(2, new Coordinates(0, -1, 0, 1), 0.5);
        one.hitBy(two, -Space.iterationTimeInSeconds / 10);
        assertEquals(5.0 / 3, one.getYVelocity(), 0.0001);
        assertEquals(-1.0 / 3, two.getYVelocity(), 0.0001);
    }

    @Test
    public void straightOnDifferentMass1() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(0, 0, -1, 0), 0.5);
        PhysicalObject two = new PhysicalObject(2, new Coordinates(-1, 0, 1, 0), 0.5);
        one.hitBy(two, -Space.iterationTimeInSeconds / 10);
        assertEquals(5.0 / 3, one.getXVelocity(), 0.0001);
        assertEquals(-1.0 / 3, two.getXVelocity(), 0.0001);
    }

    @Test
    public void straightOnDifferentMass2() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(0, 0, -1, 0), 0.5);
        PhysicalObject two = new PhysicalObject(2, new Coordinates(-1, 0, 1, 0), 0.5);
        one.hitBy(two, -Space.iterationTimeInSeconds / 10);
        assertEquals(-1.0 / 3, two.getXVelocity(), 0.0001);
        assertEquals(0, two.getYVelocity(), 0.0001);
    }

    @Test
    public void with90degImpactAngle() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(1, 0, 0, 1), sqrt(0.5));
        PhysicalObject two = new PhysicalObject(1, new Coordinates(0, 1, 1, 0), sqrt(0.5));
        one.hitBy(two, -Space.iterationTimeInSeconds / 10);
        assertEquals(1, one.getXVelocity(), 0.0001);
        assertEquals(0, one.getYVelocity(), 0.0001);
        assertEquals(0, two.getXVelocity(), 0.0001);
        assertEquals(1, two.getYVelocity(), 0.0001);
    }

    @Test
    public void with90degImpactAngleTurned() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(0, 0, 1, 1), 0.5);
        PhysicalObject two = new PhysicalObject(1, new Coordinates(1, 0, -1, 1), 0.5);
        one.hitBy(two, -Space.iterationTimeInSeconds / 10);
        assertEquals(-1, one.getXVelocity(), 0.0001);
        assertEquals(1, one.getYVelocity(), 0.0001);
        assertEquals(1, two.getXVelocity(), 0.0001);
        assertEquals(1, two.getYVelocity(), 0.0001);
    }


    @Test
    public void with45degImpactAngle() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(0, 0, 0, 0), 0.5);
        PhysicalObject two = new PhysicalObject(1, new Coordinates(-1, 1, 1, 0), 0.5);
        one.hitBy(two, -Space.iterationTimeInSeconds / 10);
        assertEquals(0.5, one.getXVelocity(), 0.0001);
        assertEquals(-0.5, one.getYVelocity(), 0.0001);
        assertEquals(0.5, two.getXVelocity(), 0.0001);
        assertEquals(0.5, two.getYVelocity(), 0.0001);
    }

    @Test
    public void with45degImpactAngleFromBelow() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(0, 0, 0, 0), 0.5);
        PhysicalObject two = new PhysicalObject(1, new Coordinates(-1, 0, 1, 1), 0.5);
        one.hitBy(two, -Space.iterationTimeInSeconds / 10);
        assertEquals(1, one.getXVelocity(), 0.0001);
        assertEquals(0, one.getYVelocity(), 0.0001);
        assertEquals(0, two.getXVelocity(), 0.0001);
        assertEquals(1, two.getYVelocity(), 0.0001);
    }

}