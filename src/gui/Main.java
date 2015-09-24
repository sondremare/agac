package gui;

import gac.GAC;
import gac.GACState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.navigation.NavigationPuzzle;
import puzzles.vertexcoloring.VertexColoringPuzzle;
import puzzles.vertexcoloring.gui.VertexColoringGUI;
import puzzles.vertexcoloring.io.file.VertexReader;
import search.*;

import java.io.File;

public class Main extends Application {

    //private NavigationPuzzle puzzle;
    private VertexColoringPuzzle puzzle;
    private GUI gui;
    private Search search;
    private File file;
    private GridPane gridPane;
    private Button startSearchButton;

    public void initPuzzle(String kValue) {
        try {
            int kVal = Integer.parseInt(kValue);
            startSearchButton.setDisable(true);
            GAC gac = VertexReader.loadFile(file, kVal);
            if (gac != null) {
                GACState gacState = new GACState(gac);
                puzzle = new VertexColoringPuzzle(gac, gacState);
                gui = new VertexColoringGUI(puzzle);
                GridPane guiRoot = gui.initGUI();
                gridPane.add(guiRoot, 1, 0);
                startSearchButton.setDisable(false);
            } else {
                startSearchButton.setDisable(true);
            }
        } catch (Exception e) {
            //do nothing
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        final FileChooser fileChooser = new FileChooser();

        Button openFileButton = new Button("Choose file");
        startSearchButton = new Button("Start Search");
        startSearchButton.setDisable(true);

        Button stopSearchButton = new Button("Stop Search");
        stopSearchButton.setDisable(true);

        Label sleepTimeLabel = new Label ("Sleep (ms): ");
        final TextField sleepTimeInput = new TextField();
        sleepTimeInput.setMaxWidth(50);
        sleepTimeInput.setText("50");

        Label kValueLabel = new Label("K: ");
        final TextField kValueInput = new TextField();
        kValueInput.setMaxWidth(50);
        kValueInput.setText("4");

        kValueInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                initPuzzle(newValue);
            }
        });

        ToggleGroup searchType = new ToggleGroup();
        RadioButton bestFirst = new RadioButton("Best-First");
        RadioButton depthFirst = new RadioButton("Depth-First");
        RadioButton breadthFirst = new RadioButton("Breadth-First");
        bestFirst.setToggleGroup(searchType);
        depthFirst.setToggleGroup(searchType);
        breadthFirst.setToggleGroup(searchType);
        bestFirst.setSelected(true);


        GridPane controlPane = new GridPane();
        controlPane.add(openFileButton, 0, 0);
        controlPane.add(bestFirst, 0, 1);
        controlPane.add(depthFirst, 0, 2);
        controlPane.add(breadthFirst, 0, 3);
        controlPane.add(sleepTimeLabel, 0, 4);
        controlPane.add(sleepTimeInput, 1, 4);
        controlPane.add(kValueLabel, 0, 5);
        controlPane.add(kValueInput, 1, 5);
        controlPane.add(startSearchButton, 0, 6);
        controlPane.add(stopSearchButton, 0, 7);

        gridPane = new GridPane();
        gridPane.setPrefSize(1200, 800);
        gridPane.add(controlPane, 0, 0);

        primaryStage.setScene(new Scene(gridPane));
        primaryStage.show();

        openFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //TODO make this puzzle independant
                File gridDirectory = new File("D:\\School\\AiProgramming\\agac\\resources\\vertex\\");
                if (gridDirectory != null && gridDirectory.exists()) {
                    fileChooser.setInitialDirectory(gridDirectory);
                }
                file = fileChooser.showOpenDialog(primaryStage);

                /*GridState state = GridReader.loadFile(file);
                if (state != null) {
                    puzzle = new NavigationPuzzle(state);
                    gui = new NavigationGUI(puzzle);
                    GridPane guiRoot = gui.initGUI();
                    gridPane.add(guiRoot, 0, 1);
                    startSearchButton.setDisable(false);
                } else {
                    startSearchButton.setDisable(true);
                }*/
                initPuzzle(kValueInput.getText());
            }
        });

        startSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (search != null) {
                    search.stop();
                }
                stopSearchButton.setDisable(false);
                Search.sleepTime = Integer.parseInt(sleepTimeInput.getText());
                if (bestFirst.isSelected()) {
                    search = new BestFirstSearch(puzzle);
                } else if (depthFirst.isSelected()) {
                    search = new DepthFirstSearch(puzzle);
                } else if (breadthFirst.isSelected()) {
                    search = new BreadthFirstSearch(puzzle);
                }

                search.initObservableLists();
                search.getObservableClosedList().addListener(new ListChangeListener<Node>() {
                    @Override
                    public void onChanged(Change<? extends Node> c) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                gui.update(search);
                            }
                        });
                    }
                });
                search.getObservableOpenList().addListener((new ListChangeListener<Node>() {
                    @Override
                    public void onChanged(Change<? extends Node> c) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                gui.update(search);
                            }
                        });
                    }
                }));

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        search.search();
                    }
                }).start();
            }
        });

        stopSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                search.stop();
                stopSearchButton.setDisable(true);
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
