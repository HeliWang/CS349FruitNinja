import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * Created by jkalpin on 2014-03-08.
 */
public class FruitFactory {

    public static Fruit getFruit() {
        Fruit f =  new Fruit(new Area(new Rectangle2D.Double(Math.random() * 100, 410, 50, 50)));
        int ticker = (int)(Math.random() * 4 + 1);

        switch(ticker) {
            case 0:
                f.setFillColor(Color.RED);
                break;
            case 1:
                f.setFillColor(Color.PINK);
                break;
            case 2:
                f.setFillColor(Color.BLUE);
                break;
            case 3:
                f.setFillColor(Color.YELLOW);
                break;
            default:
                break;
        }

        return f;
    }
}
