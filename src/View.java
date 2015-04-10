/**
 * CS349 Winter 2014
 * Assignment 3 Demo Code
 * Jeff Avery & Michael Terry
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

/*
 * View of the main play area.
 * Displays pieces of fruit, and allows players to slice them.
 */
public class View extends JPanel implements ModelListener {
    private Model model;
    private final MouseDrag drag;

    // Constructor
    View(Model m) {
        model = m;
        model.addObserver(this);

        setBackground(Color.WHITE);

        // drag represents the last drag performed, which we will need to calculate the angle of the slice
        drag = new MouseDrag();
        // add mouse listener
        addMouseListener(mouseListener);
        //addMouseMotionListener(motionListener);
    }

    // Update fired from model
    @Override
    public void update() {
        this.repaint();
        if(model.getFails() > 5) {
            model.cancelTimers();
            StringBuilder dialogMessage = new StringBuilder();
            dialogMessage.append("Your scored ").append(model.getScore()).append(" points in ").append(model.getSecondsElapsed()).append(" seconds!\n\n");
            dialogMessage.append("Play Again?");
            GameOverDialog gameOverDialog = new GameOverDialog(dialogMessage.toString(), "Game Over!");
            if(gameOverDialog.returnAnswer()) {
                model.reset();
            } else {
                System.exit(0);
            }
        }
    }

    // Panel size
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 400);
    }

    // Paint this panel
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // draw all pieces of fruit
        // note that fruit is responsible for figuring out where and how to draw itself
        for (Fruit s : model.getShapes()) {
            s.draw(g2);

            if(s.isAppeared()) {
                if(s.getTransformedShape().getBounds().getY() > 400) {
                    model.remove(s);
                }
            } else {
                if(s.getTransformedShape().getBounds().getY() < 400) {
                    s.setAppeared(true);
                }
            }
        }
    }

    // Mouse handler
    // This does most of the work: capturing mouse movement, and determining if we intersect a shape
    // Fruit is responsible for determining if it's been sliced and drawing itself, but we still
    // need to figure out what fruit we've intersected.
    private MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            drag.start(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            drag.stop(e.getPoint());

            // find intersected shapes
            for (Fruit s : model.getShapes()) {
                if (!s.isSliced() && s.intersects(drag.getStart(), drag.getEnd())) {
                    model.scorePoint();
                    try {
                        Fruit[] newFruits = s.split(drag.getStart(), drag.getEnd());
                        // add offset so we can see them split - this is used for demo purposes only!
                        // you should change so that new pieces appear close to the same position as the original piece
                        for (Fruit f : newFruits) {
                            model.add(f);
                        }
                        model.remove(s);
                    } catch (Exception ex) {
                        System.err.println("Caught error: " + ex.getMessage());
                    }
                }
            }

            drag.reset();
        }
    };

    private MouseMotionAdapter motionListener = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);

            for(Fruit f : model.getShapes()) {
                if(f.contains(e.getPoint())) {
                    drag.addFruitToDrag(f);
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
        }
    };

    /*
     * Track starting and ending positions for the drag operation
     * Needed to calculate angle of the slice
     */
    private class MouseDrag {
        private Point2D start;
        private Point2D end;
        private Set<Fruit> draggedOverFruit;

        MouseDrag() {
            draggedOverFruit = new HashSet<Fruit>();
        }

        protected void addFruitToDrag(Fruit f) {
            draggedOverFruit.add(f);
        }

        protected Set<Fruit> getDraggedOverFruit() {
            return draggedOverFruit;
        }

        protected void start(Point2D start) {
            this.start = start;
        }

        protected void stop(Point2D end) {
            this.end = end;
        }

        protected void reset() {
            start = null;
            end = null;
            draggedOverFruit = new HashSet<Fruit>();
        }

        protected Point2D getStart() {
            return start;
        }

        protected Point2D getEnd() {
            return end;
        }

    }
}
