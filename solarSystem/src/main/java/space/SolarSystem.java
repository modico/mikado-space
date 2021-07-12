package space;

import object.PhysicalObject;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class SolarSystem extends Space {

    private static final double EARTH_WEIGHT = 5.9736e24;
    private static final double ASTRONOMICAL_UNIT = 149597870.7e3;
    private static final double G = 6.67428e-11; // m3/kgs2

    @Override
    public void setupObjects(Space space) {
            space.setStepSize(3600 * 24 * 7);

            double outerLimit = ASTRONOMICAL_UNIT * 20;

            for (int i = 0; i < nrOfObjects; i++) {
                double angle = randSquare() * 2 * Math.PI;
                double radius = (0.1 + 0.9 * Math.sqrt(randSquare())) * outerLimit;
                double weightKilos = 1e3 * EARTH_WEIGHT * (Math.pow(0.00001 + 0.99999 * randSquare(), 12));
                double x = radius * Math.sin(angle);
                double y = radius * Math.cos(angle);
                double speedRandom = Math.sqrt(1 / radius) * 2978000*1500 * (0.4 + 0.6 * randSquare());

                double vx = speedRandom * Math.sin(angle - Math.PI / 2);
                double vy = speedRandom * Math.cos(angle - Math.PI / 2);
                add(weightKilos, x, y, vx, vy, 1);
            }

            scale = outerLimit / space.getWidth();

            add(EARTH_WEIGHT * 20000, 0, 0, 0, 0, 1);
    }

    @Override
    public void handleCollision(List<PhysicalObject> remove, PhysicalObject one, PhysicalObject other) {
        if (Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2)) < 5e9) {
            one.absorb(other);
            remove.add(other);
        }
    }

    @Override
    public void reverseSpeedWhenWallCollision(List<PhysicalObject> remove, PhysicalObject one) {}

    @Override
    public void stepForObjects() {
        for (PhysicalObject aff : objects) {
            double fx = 0;
            double fy = 0;
            for (PhysicalObject oth : objects) {
                if (aff == oth)
                    continue;
                double[] d = new double[]{aff.x - oth.x, aff.y - oth.y};
                double r2 = Math.pow(d[0], 2) + Math.pow(d[1], 2);
                double f = G * aff.mass * oth.mass / r2;
                double sqrtOfR2 = Math.sqrt(r2);
                fx += f * d[0] / sqrtOfR2;
                fy += f * d[1] / sqrtOfR2;
            }
            double ax = fx / aff.mass;
            double ay = fy / aff.mass;
            aff.x = aff.x - ax * Math.pow(seconds, 2) / 2 + aff.vx * seconds;
            aff.y = aff.y - ay * Math.pow(seconds, 2) / 2 + aff.vy * seconds;
            aff.vx = aff.vx - ax * seconds;
            aff.vy = aff.vy - ay * seconds;
        }
    }

    @Override
    public void paintObject(Graphics2D graphics, PhysicalObject po) {
        int diameter = po.mass >= EARTH_WEIGHT * 10000 ? 7 : 2;
        po.paintPhysicalObject(graphics,
                Color.WHITE,
                (int) ((po.x - centrex) / scale + frame.getSize().width / 2) - diameter/2,
                (int) ((po.y - centrey) / scale + frame.getSize().height / 2) - diameter/2,
                diameter);
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        new SolarSystem().runSpace();
    }

}
