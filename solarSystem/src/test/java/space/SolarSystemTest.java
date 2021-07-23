package space;

import object.Coordinates;
import object.PhysicalObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SolarSystemTest {

    @Test
    public void gravitationalFormulaIsCorrect() {
        SolarSystem s = new SolarSystem();
        s.setIterationTimeInSeconds(1);
        double earthsWeight = 5.9736e24;
        int earthsRadius = 6371000;
        PhysicalObject earth = Space.add(earthsWeight, 0, -earthsRadius, 0, 0, 1);
        PhysicalObject lump = Space.add(1, 0, 10, 0, 0, 1);
        s.moveObjects();
        Assertions.assertEquals(10 - 9.82 / 2, lump.getYPosition(), 0.02);
        Assertions.assertEquals(-9.82, lump.getYVelocity(), 0.02);
        Assertions.assertEquals(-earthsRadius, earth.getYPosition(), 0.02);
        Assertions.assertEquals(0, earth.getYVelocity(), 0.02);
        s.moveObjects();
        Assertions.assertEquals(10 - 4 * 9.82 / 2, lump.getYPosition(), 0.02);
        Assertions.assertEquals(-9.82 * 2, lump.getYVelocity(), 0.02);
        Assertions.assertEquals(-earthsRadius, earth.getYPosition(), 0.02);
        Assertions.assertEquals(0, earth.getYVelocity(), 0.02);
    }

    @Test
    public void mergeWithoutSpeed() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(1, 0, 0, 0), 1);
        PhysicalObject other = new PhysicalObject(1, new Coordinates(0, 1, 0, 0), 1);
        PhysicalObject merge = one.absorb(other);
        Assertions.assertEquals(0.5, merge.getXPosition(), 0.00001);
        Assertions.assertEquals(0.5, merge.getYPosition(), 0.00001);
        Assertions.assertEquals(0.0, merge.getXVelocity(), 0.00001);
        Assertions.assertEquals(0.0, merge.getYVelocity(), 0.00001);
    }

    @Test
    public void mergeWithSpeed() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(1, 0, 1, 0), 1);
        PhysicalObject other = new PhysicalObject(1, new Coordinates(0, 1, 0, 1), 1);
        PhysicalObject merge = one.absorb(other);
        Assertions.assertEquals(0.5, merge.getXPosition(), 0.00001);
        Assertions.assertEquals(0.5, merge.getYPosition(), 0.00001);
        Assertions.assertEquals(0.5, merge.getXVelocity(), 0.00001);
        Assertions.assertEquals(0.5, merge.getYVelocity(), 0.00001);
        Assertions.assertEquals(2, merge.getMass(), 0.00001);
    }

    @Test
    public void mergeWithSpeedAndDifferentMasses() {
        PhysicalObject one = new PhysicalObject(1, new Coordinates(1, 1, 1, 0), 1);
        PhysicalObject other = new PhysicalObject(4, new Coordinates(0, 0, 0, 1), 1);
        PhysicalObject merge = one.absorb(other);
        Assertions.assertEquals(0.2, merge.getXPosition(), 0.00001);
        Assertions.assertEquals(0.2, merge.getYPosition(), 0.00001);
        Assertions.assertEquals(0.2, merge.getXVelocity(), 0.00001);
        Assertions.assertEquals(0.8, merge.getYVelocity(), 0.00001);
        Assertions.assertEquals(5, merge.getMass(), 0.00001);
    }

    @Test
    public void headsOnMergeConservesZeroSumMomentum() {
        PhysicalObject one = new PhysicalObject(10, new Coordinates(0, 0, 100, 100), 1);
        PhysicalObject other = new PhysicalObject(100, new Coordinates(0, 0, -10, -10), 1);
        PhysicalObject merge = one.absorb(other);
        Assertions.assertEquals(0, merge.getXPosition(), 0.00001);
        Assertions.assertEquals(0, merge.getYPosition(), 0.00001);
        Assertions.assertEquals(0, merge.getXVelocity(), 0.00001);
        Assertions.assertEquals(0, merge.getYVelocity(), 0.00001);
        Assertions.assertEquals(110, merge.getMass(), 0.00001);
    }

    @Test
    public void headsOnMergeConservesMomentum() {
        PhysicalObject one = new PhysicalObject(10, new Coordinates(0, 0, 10, 10), 1);
        PhysicalObject other = new PhysicalObject(100, new Coordinates(0, 0, 0, 0), 1);
        PhysicalObject merge = one.absorb(other);
        Assertions.assertEquals(0, merge.getXPosition(), 0.00001);
        Assertions.assertEquals(0, merge.getYPosition(), 0.00001);
        Assertions.assertEquals(100 / 110.0, merge.getXVelocity(), 0.00001);
        Assertions.assertEquals(100 / 110.0, merge.getYVelocity(), 0.00001);
        Assertions.assertEquals(110, merge.getMass(), 0.00001);
    }

}
