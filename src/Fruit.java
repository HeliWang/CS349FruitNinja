/**
 * CS349 Winter 2014
 * Assignment 3 Demo Code
 * Jeff Avery & Michael Terry
 */

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * Class that represents a Fruit. Can be split into two separate fruits.
 */
public class Fruit implements FruitInterface {
    private Area            fruitShape   = null;
    private Color fillColor = Color.RED;
    private Color           outlineColor = Color.BLACK;
    private AffineTransform transform    = new AffineTransform();
    private double          outlineWidth = 5;
    private double rotationAngle = 0;

    private double xVelocity;
    private double yVelocity;
    private long timeOnScreen = 0;
    private double weight;
    private boolean appeared;
    private boolean sliced;

    /**
     * A fruit is represented using any arbitrary geometric shape.
     */
    Fruit (Area fruitShape) {
        this.fruitShape = (Area)fruitShape.clone();
        weight = 11;
        xVelocity = Math.random() * 1.5 + 0.5;
        yVelocity = Math.random() / 2 + 13;
        appeared = false;
        sliced = false;
    }

    public double getXVelocity() {
        return xVelocity;
    }

    public double getYVelocity() {
        return yVelocity;
    }

    public double getWeight() {
        return weight;
    }

    public void setXVelocity(double newVelocity) {
        xVelocity = newVelocity;
    }

    public void setYVelocity(double newVelocity) {
        yVelocity = newVelocity;
    }

    public long getTimeOnScreen() {
        return timeOnScreen;
    }

    public void incrementTimeOnScreen() {
        timeOnScreen++;
    }

    /**
     * The color used to paint the interior of the Fruit.
     */
    public Color getFillColor() {
        return fillColor;
    }
    /**
     * The color used to paint the interior of the Fruit.
     */
    public void setFillColor(Color color) {
        fillColor = color;
    }
    /**
     * The color used to paint the outline of the Fruit.
     */
    public Color getOutlineColor() {
        return outlineColor;
    }
    /**
     * The color used to paint the outline of the Fruit.
     */
    public void setOutlineColor(Color color) {
        outlineColor = color;
    }
    
    /**
     * Gets the width of the outline stroke used when painting.
     */
    public double getOutlineWidth() {
        return outlineWidth;
    }

    /**
     * Sets the width of the outline stroke used when painting.
     */
    public void setOutlineWidth(double newWidth) {
        outlineWidth = newWidth;
    }

    /**
     * Concatenates a rotation transform to the Fruit's affine transform
     */
    public void rotate(double theta) {
        rotationAngle = (rotationAngle + theta) % (Math.PI * 2);
        transform.rotate(theta);
    }

    /**
     * Concatenates a scale transform to the Fruit's affine transform
     */
    public void scale(double x, double y) {
        transform.scale(x, y);
    }

    /**
     * Concatenates a translation transform to the Fruit's affine transform
     */
    public void translate(double tx, double ty) {
        transform.translate(tx, ty);
    }

    /**
     * Returns the Fruit's affine transform that is used when painting
     */
    public AffineTransform getTransform() {
        return (AffineTransform)transform.clone();
    }

    /**
     * Creates a transformed version of the fruit. Used for painting
     * and intersection testing.
     */
    public Area getTransformedShape() {
        return fruitShape.createTransformedArea(transform);
    }

    public boolean isAppeared() {
        return appeared;
    }

    public void setAppeared(boolean appeared) {
        this.appeared = appeared;
    }

    public boolean isSliced() {
        return sliced;
    }

    public void setSliced(boolean sliced) {
        this.sliced = sliced;
    }

    /**
     * Paints the Fruit to the screen using its current affine
     * transform and paint settings (fill, outline)
     */
    public void draw(Graphics2D g2) {
        g2.setColor(getFillColor());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fill(getTransformedShape());
        g2.setColor(getOutlineColor());
        g2.setStroke(new BasicStroke((float) getOutlineWidth()));
        g2.draw(getTransformedShape());
        double translateFactor = ((this.getWeight()) * (1E-4 * ((double) (this.getTimeOnScreen() * this.getTimeOnScreen()))) - this.getYVelocity() * 1E-2 * ((double) this.getTimeOnScreen()));
        this.translate(this.getXVelocity(), translateFactor);
    }

    /**
     * Tests whether the line represented by the two points intersects
     * this Fruit.
     */
    public boolean intersects(Point2D p1, Point2D p2) {
        if (this.contains(p1) || this.contains(p2)) {
            return false;
        }
        int[] x = {(int) p1.getX(), (int) p2.getX(), (int) p2.getX(), (int) p1.getX()};
        int[] y = {(int) p1.getY() - 1, (int) p2.getY() - 1, (int) p2.getY() + 1, (int) p1.getY() + 1};

        Area slice = new Area(new Polygon(x, y, x.length));
        slice.intersect(getTransformedShape());

        return !slice.isEmpty();
    }

    /**
     * Returns whether the given point is within the Fruit's shape.
     */
    public boolean contains(Point2D p1) {
        return this.getTransformedShape().contains(p1);
    }

    /**
     * This method assumes that the line represented by the two points
     * intersects the fruit. If not, unpredictable results will occur.
     * Returns two new Fruits, split by the line represented by the
     * two points given.
     */
    public Fruit[] split(Point2D p1, Point2D p2) throws NoninvertibleTransformException {
        double deltaX = p2.getX() - p1.getX();
        double deltaY = p1.getY() - p2.getY();

        double theta = Math.atan2(deltaY, deltaX);

        Area fruitArea = this.getTransformedShape();

        AffineTransform translateTransform = new AffineTransform();
        translateTransform.translate(-p1.getX(), -p1.getY());
        fruitArea = fruitArea.createTransformedArea(translateTransform);

        AffineTransform rotateTransform = new AffineTransform();
        rotateTransform.rotate(theta);
        fruitArea = fruitArea.createTransformedArea(rotateTransform);

        System.out.println(fruitArea.getBounds().toString());

        Area bottomArea = new Area(new Rectangle(0, 0, 500, 500));
        bottomArea.intersect(fruitArea);
        bottomArea = bottomArea.createTransformedArea(rotateTransform.createInverse());
        bottomArea = bottomArea.createTransformedArea(translateTransform.createInverse());

        Area topArea = new Area(new Rectangle(0, -500, 500 ,500));
        topArea.intersect(fruitArea);
        topArea = topArea.createTransformedArea(rotateTransform.createInverse());
        topArea = topArea.createTransformedArea(translateTransform.createInverse());

        System.out.println("Top: " + topArea.getBounds().toString());
        Fruit topFruit = new Fruit(topArea);
        topFruit.setYVelocity(1);
        topFruit.setSliced(true);
        topFruit.setAppeared(true);
        topFruit.setFillColor(this.fillColor);

        System.out.println("Top: " + bottomArea.getBounds().toString());
        Fruit bottomFruit = new Fruit(bottomArea);
        bottomFruit.setYVelocity(1);
        bottomFruit.setSliced(true);
        bottomFruit.setAppeared(true);
        bottomFruit.setFillColor(this.fillColor);

        this.setSliced(true);

        if(p1.getX() > p2.getX()) {
            topFruit.setXVelocity(0.2);
            bottomFruit.setXVelocity(-0.2);
        } else {
            topFruit.setXVelocity(-0.2);
            bottomFruit.setXVelocity(0.2);
        }

        return new Fruit[] { topFruit, bottomFruit };
     }
}
