package space;

import object.PhysicalObject;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class SolarSystem extends Space {

    private static final double EARTH_WEIGHT = 5.9736e24;
    private static final double ASTRONOMICAL_UNIT = 149597870.7e3;
    public static final double OUTER_LIMIT = ASTRONOMICAL_UNIT * 20;
    private static final double G = 6.67428e-11; // m3/kgs2

    @Override
    public void setupObjects() {
            for (int i = 0; i < nrOfObjects; i++) {
                double angle = randSquare() * 2 * Math.PI;
                double radius = (0.1 + 0.9 * Math.sqrt(randSquare())) * OUTER_LIMIT;
                double weightKilos = 1e3 * EARTH_WEIGHT * (Math.pow(0.00001 + 0.99999 * randSquare(), 12));
                double x = radius * Math.sin(angle);
                double y = radius * Math.cos(angle);
                double speedRandom = Math.sqrt(1 / radius) * 2978000*1500 * (0.4 + 0.6 * randSquare());

                double vx = speedRandom * Math.sin(angle - Math.PI / 2);
                double vy = speedRandom * Math.cos(angle - Math.PI / 2);
                add(weightKilos, x, y, vx, vy, 1);
            }
            add(EARTH_WEIGHT * 20000, 0, 0, 0, 0, 1);
    }

    @Override
    public void handleCollision(List<PhysicalObject> remove, PhysicalObject one, PhysicalObject other) {
        if (isCollision(one, other, 5e9)) {
            one.absorb(other);
            remove.add(other);
        }
    }

    @Override
    public void reverseSpeedWhenWallCollision(List<PhysicalObject> remove, PhysicalObject one) {}

    @Override
    public void setCoordinatesOfObjects() {
        for (PhysicalObject aff : objects) {
            double fx = 0;
            double fy = 0;
            for (PhysicalObject oth : objects) {
                if (aff == oth)
                    continue;
                double[] d = new double[]{aff.getXPosition() - oth.getXPosition(), aff.getYPosition() - oth.getYPosition()};
                double r2 = Math.pow(d[0], 2) + Math.pow(d[1], 2);
                double f = G * aff.getMass() * oth.getMass() / r2;
                double sqrtOfR2 = Math.sqrt(r2);
                fx += f * d[0] / sqrtOfR2;
                fy += f * d[1] / sqrtOfR2;
            }
            double ax = fx / aff.getMass();
            double ay = fy / aff.getMass();
            aff.setXPosition(aff.getXPosition() - ax * Math.pow(iterationTimeInSeconds, 2) / 2 + aff.getXVelocity() * iterationTimeInSeconds);
            aff.setYPosition(aff.getYPosition() - ay * Math.pow(iterationTimeInSeconds, 2) / 2 + aff.getYVelocity() * iterationTimeInSeconds);
            aff.setXVelocity(aff.getXVelocity() - ax * iterationTimeInSeconds);
            aff.setYVelocity(aff.getYVelocity() - ay * iterationTimeInSeconds);
        }
    }

    @Override
    public void paintObject(Graphics2D graphics, PhysicalObject objectToPaint) {
        int diameter = objectToPaint.getMass() >= EARTH_WEIGHT * 10000 ? 7 : 2;
        objectToPaint.paintPhysicalObject(graphics,
                Color.WHITE,
                (int) ((objectToPaint.getXPosition() - centreX) / scale + frame.getSize().width / 2) - diameter/2,
                (int) ((objectToPaint.getYPosition() - centreY) / scale + frame.getSize().height / 2) - diameter/2,
                diameter);
    }

    @Override
    public void setupSpaceParameters() {
        setIterationTimeInSeconds(3600 * 24 * 7);
        scale = OUTER_LIMIT / getWidth();
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        new SolarSystem().runSpace();
    }

}
