package simulation;

import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * klasa glowna programu
 */
public class Symulacja extends Application {

    /** generator liczb losowych */
    public static Random generator = new Random();
    /** szerokosc */
    public static int n;
    /** wysokosc */
    public static int m;
    /** prawdopodobienstwo */
    public static double p;
    /** sredni czas w milisekundach */
    public static double k;
    /** dwuwymiarowa lista kwadratów */
    public static MyRectangle[][] rectangles;
    /** dwuwymiarowa lista procesów */
    public static Thread[][] threads;

    public static void main(String[] args) {
        // sczytuje parametry z konsoli
        try {
            n = Integer.parseInt(args[0]);
            m = Integer.parseInt(args[1]);
            p = Double.parseDouble(args[2]);
            k = Double.parseDouble(args[3]);
            rectangles = new MyRectangle[n][m];
            threads = new Thread[n][m];

        } catch (Exception e) {
            System.out.println("Prawidlowe uzycie: Symulacja n, m, prawdopodobieństwo, czas zmiany (ms)");
            System.exit(1);
        }
        if (p < 0 || p > 1) {
            System.out.println("p ma byc miedzy 0 a 1");
            System.exit(1);
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Pane pane = new Pane();
        // tworze wszystkie kwadraty i watki
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rectangles[i][j] = new MyRectangle(i, j);
                pane.getChildren().add(rectangles[i][j]);
                threads[i][j] = new Thread(rectangles[i][j]);
            }
        }
        // ustawiam sasiadow kwadratow
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rectangles[i][j].setNeighbours(i, j);
            }
        }
        // uruchamiam watki
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                threads[i][j].setDaemon(true);
                threads[i][j].start();
            }
        }
        // specyfikacja okna
        Scene scene = new Scene(pane, 20 * n, 20 * m);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Symulacja");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
