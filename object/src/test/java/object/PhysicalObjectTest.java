package object;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PhysicalObjectTest {

    @Test
    public void mergeWithoutSpeed() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(1, 0, 0, 0), 1);
        PhysicalObject other = new PhysicalObject(1, new Coordinates(0, 1, 0, 0), 1);
        PhysicalObject merge = one.absorb(other);
        assertEquals(0.5, merge.getXPosition(), 0.00001);
        assertEquals(0.5, merge.getYPosition(), 0.00001);
        assertEquals(0.0, merge.getXVelocity(), 0.00001);
        assertEquals(0.0, merge.getYVelocity(), 0.00001);
    }

    @Test
    public void mergeWithSpeed() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(1, 0, 1, 0), 1);
        PhysicalObject other = new PhysicalObject(1, new Coordinates(0, 1, 0, 1), 1);
        PhysicalObject merge = one.absorb(other);
        assertEquals(0.5, merge.getXPosition(), 0.00001);
        assertEquals(0.5, merge.getYPosition(), 0.00001);
        assertEquals(0.5, merge.getXVelocity(), 0.00001);
        assertEquals(0.5, merge.getYVelocity(), 0.00001);
        assertEquals(2, merge.getMass(), 0.00001);
    }

    @Test
    public void mergeWithSpeedAndDifferentMasses() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(1, 1, 1, 0), 1);
        PhysicalObject other = new PhysicalObject(4, new Coordinates(0, 0, 0, 1), 1);
        PhysicalObject merge = one.absorb(other);
        assertEquals(0.2, merge.getXPosition(), 0.00001);
        assertEquals(0.2, merge.getYPosition(), 0.00001);
        assertEquals(0.2, merge.getXVelocity(), 0.00001);
        assertEquals(0.8, merge.getYVelocity(), 0.00001);
        assertEquals(5, merge.getMass(), 0.00001);
    }

    @Test
    public void headsOnMergeConservesZeroSumMomentum() {
        PhysicalObject one = new PhysicalObject(10, new Coordinates(0, 0, 100, 100), 1);
        PhysicalObject other = new PhysicalObject(100, new Coordinates(0, 0, -10, -10), 1);
        PhysicalObject merge = one.absorb(other);
        assertEquals(0, merge.getXPosition(), 0.00001);
        assertEquals(0, merge.getYPosition(), 0.00001);
        assertEquals(0, merge.getXVelocity(), 0.00001);
        assertEquals(0, merge.getYVelocity(), 0.00001);
        assertEquals(110, merge.getMass(), 0.00001);
    }

    @Test
    public void headsOnMergeConservesMomentum() {
        PhysicalObject one = new PhysicalObject(10, new Coordinates(0, 0, 10, 10), 1);
        PhysicalObject other = new PhysicalObject(100, new Coordinates(0, 0, 0, 0), 1);
        PhysicalObject merge = one.absorb(other);
        assertEquals(0, merge.getXPosition(), 0.00001);
        assertEquals(0, merge.getYPosition(), 0.00001);
        assertEquals(100 / 110.0, merge.getXVelocity(), 0.00001);
        assertEquals(100 / 110.0, merge.getYVelocity(), 0.00001);
        assertEquals(110, merge.getMass(), 0.00001);
    }

}