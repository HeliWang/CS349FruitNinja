/**
 * CS349 Winter 2014
 * Assignment 3 Demo Code
 * Jeff Avery & Michael Terry
 */

import javax.swing.*;
import java.awt.*;

/*
 * View to display the Title, and Score
 * Score currently just increments every time we get an update
 * from the model (i.e. a new fruit is added).
 */
public class TitleView extends JPanel implements ModelListener {
    private Model model;
    private JLabel title, score, timeElapsed, fails;

    // Constructor requires model reference
    TitleView(Model model) {
        // register with model so that we get updates
        this.model = model;
        this.model.addObserver(this);

        // draw something
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.YELLOW);
        // You may want a better name for this game!
        title = new JLabel(" Square NINJA!          ");
        score = new JLabel();
        timeElapsed = new JLabel();
        fails = new JLabel();

        // use border layout so that we can position labels on the left and right
        this.setLayout(new BorderLayout());
        this.add(title, BorderLayout.WEST);
        this.add(score, BorderLayout.EAST);
        this.add(timeElapsed, BorderLayout.CENTER);
    }

    // Panel size
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 35);
    }

    // Update from model
    // This is ONLY really useful for testing that the view notifications work
    // You likely want something more meaningful here.
    @Override
    public void update() {
        paint(getGraphics());
    }

    // Paint method
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        score.setText("Fails: " + model.getFails() + "  Count: " + model.getScore() + "  ");
        timeElapsed.setText("Time Elapsed: " + model.getMinutesElapsed() + " : " + model.getSecondsElapsed() % 60);
    }
}
