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
    public void setupObjects() {
        nrOfObjects = 50;
        for (int i = 0; i < nrOfObjects; i++) {
            // radius,weight in [1,20]
            double radiusAndWeight = 1 + 19 * Math.random();
            //x,y in [max radius, width or height - max radius]
            add(radiusAndWeight, 20 + 760 * Math.random(), 20 + 760 * Math.random(), 3 - 6 * Math.random(), 3 - 6 * Math.random(), radiusAndWeight);
        }

    }

    @Override
    public void handleCollision(List<PhysicalObject> remove, PhysicalObject one, PhysicalObject other) {
        if (isCollision(one,other,one.getRadius() + other.getRadius())) {
            one.hitBy(other, -iterationTimeInSeconds / 10);
        }
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {}

    @Override
    public void mouseDragged(final MouseEvent e) {}

    @Override
    public void reverseSpeedWhenWallCollision(List<PhysicalObject> remove, PhysicalObject one) {
        if (one.getXPosition() - one.getRadius() < 0) {
            one.setXVelocity(-one.getXVelocity());
        }
        if (one.getXPosition() + one.getRadius() > 800) {
            one.setXVelocity(-one.getXVelocity());
        }
        if (one.getYPosition() - one.getRadius() < 0) {
            one.setYVelocity(-one.getYVelocity());
        }
        if (one.getYPosition() + one.getRadius() > 800 && !IS_BREAKOUT) {
            one.setYVelocity(-one.getYVelocity());
        } else if (one.getYPosition() - one.getRadius() > 800) {
            remove.add(one);
        }
    }

    @Override
    public void setCoordinatesOfObjects() {
        for (PhysicalObject physicalObject : objects) {
            physicalObject.setXPosition(physicalObject.getXPosition() + physicalObject.getXVelocity() * iterationTimeInSeconds);
            physicalObject.setYPosition(physicalObject.getYPosition() + physicalObject.getYVelocity() * iterationTimeInSeconds);
        }
    }

    @Override
    public void paintObject(Graphics2D graphics, PhysicalObject objectToPaint) {
        objectToPaint.paintPhysicalObject(graphics,
                weightToColor(objectToPaint.getMass()),
                (int) ((objectToPaint.getXPosition() - centreX)  + frame.getSize().width / 2 - objectToPaint.getRadius()),
                (int) ((objectToPaint.getYPosition() - centreY)  + frame.getSize().height / 2 - objectToPaint.getRadius()),
                (int) (2 * objectToPaint.getRadius()));
    }

    @Override
    public void setupSpaceParameters() {
        setIterationTimeInSeconds(1); // One second per iteration
        scale = 1;
        centreX = 400;
        centreY = 390; //Must compensate for title bar
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
