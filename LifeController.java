import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.*;



public class LifeController extends Application {
  
    // update this constant to speed up or slow down the simulation.
    public static final double SECONDS_PER_GENERATION = .1;

    private ObservableList<String> fileListOptions;
    private Label generationLabel = new Label();
    private ComboBox<String> fileListBox;
    private Life life;

    public static void main(String[] args) {
      launch(args);
    }

    private ObservableList<String> getFileListOptions() {
      ObservableList<String> list = FXCollections.observableArrayList();

      File dir = new File(".");
      File[] fileList = dir.listFiles();

      for (File f : fileList) {
        if (f.isFile()) {
          String name = f.getName();
          String extension = name.substring(name.length()-3).toLowerCase();
          if (extension.equals("txt")) {
            list.add(f.getName());
          }
        }
      }
      Collections.sort(list);
      return list;
    }

    @Override
    public void init() throws Exception {
        super.init();
        fileListOptions = getFileListOptions();
        fileListBox = new ComboBox<>(fileListOptions);
        fileListBox.setValue(fileListOptions.get(0));
        life = new Life(fileListOptions.get(0));
    }

    @Override
    public void start(Stage stage) {
        final Canvas canvas = new Canvas(500, 500);
        BorderPane bPane = new BorderPane();
        bPane.setTop(generationLabel);
        bPane.setBottom(fileListBox);
        bPane.setCenter(canvas);


        fileListBox.valueProperty().addListener((ov, oldValue, newValue) -> {
                life = new Life((String)newValue);
            }
        );
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(SECONDS_PER_GENERATION), e -> {
                    life.passTime();

                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    double w = gc.getCanvas().getWidth();
                    double h = gc.getCanvas().getHeight();
                    double cw = w / Life.MAX_COLUMNS;
                    double ch = h / Life.MAX_ROWS;

                    gc.setFill(Color.CORNSILK);
                    gc.fillRect(0, 0, w, h);

                    gc.setFill(Color.FORESTGREEN);

                    for (int i = 0; i < Life.MAX_ROWS; i++) {
                        for (int j = 0; j < Life.MAX_COLUMNS; j++) {
                            if (life.isAlive (i, j)) {
                                double y = ch * i;
                                double x = cw * j;
                                gc.fillRect(x, y, cw, ch);
                            }
                        }
                    }
                    generationLabel.setText(fileListBox.getValue() + "; Generation: " + life.getGeneration());

                }));
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);

        stage.setTitle("Life");
        stage.setScene(new Scene(bPane));
        stage.show();

        timeline.play();
    }
}
