package space;

import object.Coordinates;
import object.PhysicalObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class Space extends JFrame implements MouseWheelListener,
        MouseMotionListener, KeyListener {

    private static final long serialVersionUID = 1532817796535372081L;

    public static double iterationTimeInSeconds = 1;
    public static List<PhysicalObject> objects = new ArrayList<>();
    static double centreX = 0.0;
    static double centreY = 0.0;
    static double scale = 10;
    private static boolean showWake = false;
    public static int step = 0;
    public static int nrOfObjects = 75;
    private static int frameRate = 25;

    static JFrame frame;

    public Space() {
        setBackground(Color.BLACK);
        Space.frame = this;
    }

    @Override
    public void paint(Graphics original) {
        if (original != null) {
            BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = buffer.createGraphics();
            if (!showWake) {
                graphics.clearRect(0, 0, getWidth(), getHeight());
            }
            for (PhysicalObject po : objects) {
                paintObject(graphics, po);
                String string = "Objects:" + objects.size() + " scale:" + scale + " steps:" + step + " frame rate: " + frameRate;
                setTitle(string);
            }
            original.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
        }
    }

    public abstract void paintObject(Graphics2D graphics, PhysicalObject objectToPaint);

    public abstract void setupSpaceParameters();

    public void runSpace() throws InterruptedException, InvocationTargetException {

        this.addMouseWheelListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.setSize(800, 820);
        this.setupSpaceParameters();
        this.setupObjects();
        this.setVisible(true);
        while (true) {
            final long start = System.currentTimeMillis();
            EventQueue.invokeAndWait(() -> {
                this.collide();
                this.moveObjects();
            });
            try {
                long ahead = 1000 / frameRate - (System.currentTimeMillis() - start);
                if (ahead > 50) {
                    Thread.sleep(ahead);
                    if(frameRate<25) frameRate++;
                } else {
                    Thread.sleep(50);
                    frameRate--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setupObjects() {}

    public static double randSquare() {
        double random = Math.random();
        return random * random;
    }

    public void setIterationTimeInSeconds(double seconds) {
        Space.iterationTimeInSeconds = seconds;
    }

    public static PhysicalObject add(double weightKilos, double x, double y,
                                     double vx, double vy, double radius) {
        PhysicalObject physicalObject = new PhysicalObject(weightKilos,
                new Coordinates(x, y, vx, vy), radius);
        objects.add(physicalObject);
        return physicalObject;
    }

    public void moveObjects() {
        setCoordinatesOfObjects();
        step++;
        paint(getGraphics());
    }

    public abstract void setCoordinatesOfObjects();

    private void collide() {
        List<PhysicalObject> remove = new ArrayList<>();
        for (PhysicalObject one : objects) {
            if (remove.contains(one))
                continue;
            for (PhysicalObject other : objects) {
                if (one == other || remove.contains(other))
                    continue;
                handleCollision(remove, one, other);
            }
            // Wall collision reverses speed in that direction
            reverseSpeedWhenWallCollision(remove, one);
        }
        objects.removeAll(remove);
    }

    public abstract void reverseSpeedWhenWallCollision(List<PhysicalObject> remove, PhysicalObject one);

    public abstract void handleCollision(List<PhysicalObject> remove, PhysicalObject one, PhysicalObject other);

    public boolean isCollision(PhysicalObject one, PhysicalObject other, double limit) {
        return Math.sqrt(Math.pow(one.getXPosition() - other.getXPosition(), 2) + Math.pow(one.getYPosition() - other.getYPosition(), 2)) < limit;
    }

    public void mouseWheelMoved(final MouseWheelEvent e) {
        scale = scale + scale * (Math.min(9, e.getWheelRotation())) / 10 + 0.0001;
        getGraphics().clearRect(0, 0, getWidth(), getHeight());
    }

    private static Point lastDrag = null;

    public void mouseDragged(final MouseEvent e) {
        if (lastDrag == null) {
            lastDrag = e.getPoint();
        }
        centreX = centreX - ((e.getX() - lastDrag.x) * scale);
        centreY = centreY - ((e.getY() - lastDrag.y) * scale);
        lastDrag = e.getPoint();
        getGraphics().clearRect(0, 0, getWidth(), getHeight());
    }

    public void mouseMoved(MouseEvent e) {
        lastDrag = null;
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'w')
            showWake = !showWake;
    }

}
