package space;

import object.PhysicalObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class BouncingBalls extends Space {
    private static final boolean IS_BREAKOUT = false;

    @Override
    public void setupObjects(Space space) {
        nrOfObjects = 50;
        space.setStepSize(1); // One second per iteration
        for (int i = 0; i < nrOfObjects; i++) {
            // radius,weight in [1,20]
            double radiusAndWeight = 1 + 19 * Math.random();
            //x,y in [max radius, width or height - max radius]
            Space.add(radiusAndWeight, 20 + 760 * Math.random(), 20 + 760 * Math.random(), 3 - 6 * Math.random(), 3 - 6 * Math.random(), radiusAndWeight);
        }
        scale = 1;
        centrex = 400;
        centrey = 390; //Must compensate for title bar
    }

    @Override
    public void handleCollision(List<PhysicalObject> remove, PhysicalObject one, PhysicalObject other) {
        double distance = Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2));
        double collsionDistance = one.radius + other.radius;
        if (distance < collsionDistance) {
            one.hitBy(other, -seconds / 10);
        }
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {}

    @Override
    public void mouseDragged(final MouseEvent e) {}

    @Override
    public void reverseSpeedWhenWallCollision(List<PhysicalObject> remove, PhysicalObject one) {
        if (one.x - one.radius < 0) {
            one.vx = -one.vx;
        }
        if (one.x + one.radius > 800) {
            one.vx = -one.vx;
        }
        if (one.y - one.radius < 0) {
            one.vy = -one.vy;
        }
        if (one.y + one.radius > 800 && !IS_BREAKOUT) {
            one.vy = -one.vy;
        } else if (one.y - one.radius > 800) {
            remove.add(one);
        }
    }

    @Override
    public void stepForObjects() {
        for (PhysicalObject physicalObject : objects) {
            physicalObject.x = physicalObject.x + physicalObject.vx * seconds;
            physicalObject.y = physicalObject.y + physicalObject.vy * seconds;
        }
    }

    @Override
    public void paintObject(Graphics2D graphics, PhysicalObject po) {
        po.paintPhysicalObject(graphics,
                weightToColor(po.mass),
                (int) ((po.x - centrex)  + frame.getSize().width / 2 - po.radius),
                (int) ((po.y - centrey)  + frame.getSize().height / 2 - po.radius),
                (int) (2 * po.radius));
    }

    private static Color weightToColor(double weight) {
        if (weight < 10) return Color.GREEN;
        if (weight < 12) return Color.CYAN;
        if (weight < 14) return Color.MAGENTA;
        if (weight < 16) return Color.BLUE;
        if (weight < 18) return Color.GRAY;
        if (weight < 20) return Color.RED;
        if (weight < 22) return Color.ORANGE;
        if (weight < 25) return Color.PINK;
        if (weight < 28) return Color.YELLOW;
        return Color.WHITE;
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        new BouncingBalls().runSpace();
    }

}
