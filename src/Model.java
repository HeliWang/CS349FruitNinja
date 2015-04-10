/**
 * CS349 Winter 2014
 * Assignment 3 Demo Code
 * Jeff Avery & Michael Terry
 */

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/*
 * Class the contains a list of fruit to display.
 * Follows MVC pattern, with methods to add observers,
 * and notify them when the fruit list changes.
 */
public class Model {
    // Observer list
    private Vector<ModelListener> views = new Vector();

    // Fruit that we want to display
    private ArrayList<Fruit> shapes = new ArrayList();

    private int score = 0;
    private int fails = 0;
    private Timer clock;
    private Timer physicsClock;
    private long secondsElapsed = 0;

    // Constructor
    Model() {
        shapes.clear();
        clock = new Timer();
        clock.scheduleAtFixedRate(new ModelTimerTask(), 0, 1000);
        physicsClock = new Timer();
        physicsClock.scheduleAtFixedRate(new PhysicsTimerTask(), 0, 16);
    }

    private void updateClock() {
        secondsElapsed++;
    }

    // MVC methods
    // These likely don't need to change, they're just an implementation of the
    // basic MVC methods to bind view and model together.
    public void addObserver(ModelListener view) {
        views.add(view);
    }

    public void notifyObservers() {
        for (ModelListener v : views) {
            v.update();
        }
    }

    // Model methods
    // You may need to add more methods here, depending on required functionality.
    // For instance, this sample makes to effort to discard fruit from the list.
    public void add(Fruit s) {
        shapes.add(s);
    }

    public void remove(Fruit s) {
        shapes.remove(s);

        if(!s.isSliced()) {
            failed();
        }
    }

    public void cancelTimers() {
        clock.cancel();
        physicsClock.cancel();
    }

    public void reset() {
        score = 0;
        fails = 0;
        secondsElapsed = 0;
        shapes.clear();
        clock = new Timer();
        clock.scheduleAtFixedRate(new ModelTimerTask(), 0, 1000);
        physicsClock = new Timer();
        physicsClock.scheduleAtFixedRate(new PhysicsTimerTask(), 0, 16);
    }

    public int getScore() {
        return score;
    }

    public void scorePoint() {
        score++;
    }

    public int getFails() {
        return fails;
    }

    public void failed() {
        fails++;
    }

    public long getSecondsElapsed() {
        return secondsElapsed;
    }

    public long getMinutesElapsed() {
        return secondsElapsed / 60;
    }

    public void updateTimeOnScreen() {
        for (Fruit f : shapes) {
            f.incrementTimeOnScreen();
        }
    }

    public ArrayList<Fruit> getShapes() {
        return (ArrayList<Fruit>) shapes.clone();
    }

    private class ModelTimerTask extends TimerTask {
        long ticker = 0;
        int randomOffset = (int)Math.random() * 4 + 1;
        public void run() {
            updateClock();

            if(ticker % randomOffset == 0) {
                add(FruitFactory.getFruit());
                randomOffset = (int) Math.random() * 4 + 1;
            }

            ticker++;

        }
    }

    private class PhysicsTimerTask extends TimerTask {
        @Override
        public void run() {
            updateTimeOnScreen();
            notifyObservers();
        }
    }
}
