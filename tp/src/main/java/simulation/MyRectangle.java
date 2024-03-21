package simulation;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * klasa kwadratu
 */
public class MyRectangle extends Rectangle implements Runnable {

    /** sasiedzi kwadratu */
    MyRectangle[] neighbours = new MyRectangle[4];
    /** wartosc koloru czerwonego */
    double red = Symulacja.generator.nextDouble();
    /** wartosc koloru zielonego */
    double green = Symulacja.generator.nextDouble();
    /** wartosc koloru niebieskiego */
    double blue = Symulacja.generator.nextDouble();
    /** pozycja x i y kwadratu */
    int[] id = new int[2];
    /** stan aktywnosci */
    boolean active = true;

    /**
     * konstruktor
     * 
     * @param x pozycja x kwadratu
     * @param y pozycja y kwadratu
     */
    public MyRectangle(int x, int y) {
        super(20 * x, 20 * y, 20, 20);
        id[0] = x;
        id[1] = y;
        setFill(Color.color(red, green, blue));
        setStroke(Color.BLACK);
        setStrokeWidth(0);
        setOnMouseClicked(new MyMouseHandler());
    }

    /**
     * ustawia losowy kolor kwadratu
     */
    private void setRandomFill() {
        red = Symulacja.generator.nextDouble();
        green = Symulacja.generator.nextDouble();
        blue = Symulacja.generator.nextDouble();
        setFill(Color.color(red, green, blue));
    }

    /**
     * ustawia kolor kwadratu jako srednia kolorow sasiadow
     */
    private void setNeighbourFill() {
        double avg_red = 0;
        double avg_green = 0;
        double avg_blue = 0;
        int num_neighbours = 0;

        for (int i = 0; i < 4; i++) {
            if (neighbours[i].active) {
                avg_red += neighbours[i].red;
                avg_green += neighbours[i].green;
                avg_blue += neighbours[i].blue;
                num_neighbours++;
            }
        }
        if (num_neighbours == 0) {
            return;
        }
        red = avg_red / num_neighbours;
        green = avg_green / num_neighbours;
        blue = avg_blue / num_neighbours;
        setFill(Color.color(red, green, blue));

    }

    /**
     * ustawia referencje do sasiadow kwadratu
     * (w przypadku krawedzi zawija do torusa)
     * 
     * @param x pozycja x kwadratu
     * @param y pozycja y kwadratu
     */
    public void setNeighbours(int x, int y) {
        if (x == 0) {
            neighbours[0] = Symulacja.rectangles[Symulacja.n - 1][y];
        } else {
            neighbours[0] = Symulacja.rectangles[x - 1][y];
        }
        if (x == Symulacja.n - 1) {
            neighbours[1] = Symulacja.rectangles[0][y];
        } else {
            neighbours[1] = Symulacja.rectangles[x + 1][y];
        }
        if (y == 0) {
            neighbours[2] = Symulacja.rectangles[x][Symulacja.m - 1];
        } else {
            neighbours[2] = Symulacja.rectangles[x][y - 1];
        }
        if (y == Symulacja.m - 1) {
            neighbours[3] = Symulacja.rectangles[x][0];
        } else {
            neighbours[3] = Symulacja.rectangles[x][y + 1];
        }
    }

    /**
     * zmienia kolor kwadratu na losowyz prawdopodobienstwem p
     * lub na srednia kolorow sasiadow inaczej
     * (jesli kwadrat jest nieaktywny, metoda nic nie robi)
     */
    private void changeFill() {
        if (active) {
            //System.out.println("start X: " + id[0] + " Y: " + id[1]);
            if (Symulacja.generator.nextDouble() <= Symulacja.p) {
                setRandomFill();
            } else {
                setNeighbourFill();
            }
            //System.out.println("end X: " + id[0] + " Y: " + id[1]);
            //System.out.println("---------------------");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                // watek czeka na czas od 0.5k do 1.5k
                Thread.sleep((long) ((0.5 + Symulacja.generator.nextDouble()) * Symulacja.k));
            } catch (InterruptedException e) {
            }
            // synchronizacja watkow
            synchronized (Symulacja.rectangles) {
                changeFill();
            }
        }
    }

    /**
     * obsluga myszy
     * (odpowiada za aktywacje i deaktywacje kwadratow)
     */
    class MyMouseHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent e) {
            MyRectangle rectangle = (MyRectangle) e.getSource();
            if (rectangle.active == true) {
                rectangle.active = false;
                rectangle.setStrokeWidth(2);
            } else {
                rectangle.active = true;
                rectangle.setStrokeWidth(0);
            }
        }

    }

}
