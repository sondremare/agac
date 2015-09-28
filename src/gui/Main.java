package gui;

import gac.*;
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
import puzzles.navigation.GridState;
import puzzles.navigation.NavigationPuzzle;
import puzzles.navigation.gui.NavigationGUI;
import puzzles.navigation.io.file.GridReader;
import puzzles.nonogram.NonogramPuzzle;
import puzzles.nonogram.gui.NonogramGUI;
import puzzles.nonogram.io.file.NonogramReader;
import puzzles.vertexcoloring.VertexColoringPuzzle;
import puzzles.vertexcoloring.gui.VertexColoringGUI;
import puzzles.vertexcoloring.io.file.VertexReader;
import search.*;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    private Puzzle puzzle;
    private GUI gui;
    private Search search;
    private File file;
    private GridPane gridPane;
    private GridPane controlPane;
    private GridPane guiRoot;
    private Button startSearchButton;
    private ComboBox puzzleSelect;
    private Label kValueLabel;
    private TextField kValueInput;
    private String activeResourcePath = navigationResourcePath;
    private Label openCountLabel;
    private Label closedCountLabel;
    private Label solutionLengthLabel;
    private Label violatedConstraintsLabel;
    private Label unassignedDomainsLabel;
    private GridPane infoPane;

    private static final String navigationResourcePath = "D:\\School\\AiProgramming\\agac\\resources\\navigation\\";
    private static final String vertexResourcePath = "D:\\School\\AiProgramming\\agac\\resources\\vertex\\";
    private static final String nonogramResourcePath = "D:\\School\\AiProgramming\\agac\\resources\\nonogram\\";

    public void initPuzzle() {
        gridPane.getChildren().remove(guiRoot);
        startSearchButton.setDisable(true);
        PuzzleType puzzleType = (PuzzleType) puzzleSelect.getValue();
        switch (puzzleType) {
            case NAVIGATION:
                GridState state = GridReader.loadFile(file);
                if (state != null) {
                    puzzle = new NavigationPuzzle(state);
                    gui = new NavigationGUI(puzzle);
                }
                break;
            case VERTEX_COLORING:
                int kVal = Integer.parseInt(kValueInput.getText());
                GAC gac = VertexReader.loadFile(file, kVal);
                if (gac != null) {
                    GACState gacState = new GACState(gac);
                    puzzle = new VertexColoringPuzzle(gac, gacState);
                    gui = new VertexColoringGUI(puzzle);
                }
                break;
            case NONOGRAM:
                GAC nonoGac = NonogramReader.loadFile(file);
                if (nonoGac != null) {
                    GACState gacState = new GACState(nonoGac);
                    puzzle = new NonogramPuzzle(nonoGac, gacState);
                    gui = new NonogramGUI(puzzle);
                }
                break;
        }
        if (puzzle != null && gui != null) {
            guiRoot = gui.initGUI();
            gridPane.add(guiRoot, 1, 0);
            startSearchButton.setDisable(false);
        }
        initInfoLabels();
    }

    public void initInfoLabels() {
        if (infoPane != null) {
            controlPane.getChildren().remove(infoPane);
        }
        openCountLabel = new Label("Open: ");
        closedCountLabel = new Label("Closed: ");
        solutionLengthLabel = new Label("Solution length: ");
        violatedConstraintsLabel = new Label("Violated constraints: ");
        unassignedDomainsLabel = new Label("Non-deduced domains: ");

        infoPane = new GridPane();
        infoPane.add(openCountLabel, 0, 0);
        infoPane.add(closedCountLabel, 0, 1);
        infoPane.add(solutionLengthLabel, 0, 2);
        PuzzleType puzzleType = (PuzzleType) puzzleSelect.getValue();
        if (puzzleType == PuzzleType.VERTEX_COLORING || puzzleType == PuzzleType.NONOGRAM) {
            infoPane.add(violatedConstraintsLabel, 0, 3);
            infoPane.add(unassignedDomainsLabel, 0, 4);
        }

        controlPane.add(infoPane, 0, 9);
    }

    public void addKInputField() {
        kValueLabel = new Label("K: ");
        kValueInput = new TextField();
        kValueInput.setMaxWidth(50);
        kValueInput.setText("4");

        kValueInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                initPuzzle();
            }
        });
        controlPane.add(kValueLabel, 0, 6);
        controlPane.add(kValueInput, 1, 6);
    }

    public void removeKInputField() {
        controlPane.getChildren().remove(kValueLabel);
        controlPane.getChildren().remove(kValueInput);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        controlPane = new GridPane();
        final FileChooser fileChooser = new FileChooser();

        puzzleSelect = new ComboBox();
        puzzleSelect.getItems().setAll(PuzzleType.values());
        puzzleSelect.getSelectionModel().selectFirst();
        puzzleSelect.valueProperty().addListener(new ChangeListener<PuzzleType>() {

            @Override
            public void changed(ObservableValue<? extends PuzzleType> observable, PuzzleType oldValue, PuzzleType newValue) {
                if (newValue == PuzzleType.NAVIGATION) {
                    removeKInputField();
                    activeResourcePath = navigationResourcePath;
                } else if (newValue == PuzzleType.VERTEX_COLORING) {
                    addKInputField();
                    activeResourcePath = vertexResourcePath;
                } else if (newValue == PuzzleType.NONOGRAM) {
                    removeKInputField();
                    activeResourcePath = nonogramResourcePath;
                }
            }
        });

        Button openFileButton = new Button("Choose file");
        startSearchButton = new Button("Start Search");
        startSearchButton.setDisable(true);

        Button stopSearchButton = new Button("Stop Search");
        stopSearchButton.setDisable(true);

        Label sleepTimeLabel = new Label ("Sleep (ms): ");
        final TextField sleepTimeInput = new TextField();
        sleepTimeInput.setMaxWidth(50);
        sleepTimeInput.setText("50");

        ToggleGroup searchType = new ToggleGroup();
        RadioButton bestFirst = new RadioButton("Best-First");
        RadioButton depthFirst = new RadioButton("Depth-First");
        RadioButton breadthFirst = new RadioButton("Breadth-First");
        bestFirst.setToggleGroup(searchType);
        depthFirst.setToggleGroup(searchType);
        breadthFirst.setToggleGroup(searchType);
        bestFirst.setSelected(true);



        controlPane.add(puzzleSelect, 0, 0);
        controlPane.add(openFileButton, 0, 1);
        controlPane.add(bestFirst, 0, 2);
        controlPane.add(depthFirst, 0, 3);
        controlPane.add(breadthFirst, 0, 4);
        controlPane.add(sleepTimeLabel, 0, 5);
        controlPane.add(sleepTimeInput, 1, 5);
        controlPane.add(startSearchButton, 0, 7);
        controlPane.add(stopSearchButton, 0, 8);

        gridPane = new GridPane();
        gridPane.setPrefSize(1400, 800);
        gridPane.add(controlPane, 0, 0);

        primaryStage.setScene(new Scene(gridPane));
        primaryStage.show();

        openFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File gridDirectory = new File(activeResourcePath);
                if (gridDirectory != null && gridDirectory.exists()) {
                    fileChooser.setInitialDirectory(gridDirectory);
                }
                file = fileChooser.showOpenDialog(primaryStage);
                initPuzzle();
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
                                updateLabels(search);
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
                                updateLabels(search);
                            }
                        });
                    }
                }));

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        if (search.search()) {
                            showMessage("Search successful");
                        } else {
                            showMessage("Could not find a solution");
                        }

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

    public void updateLabels(Search search) {
        Node currentNode = search.getCurrentNode();
        if (currentNode != null) {
            PuzzleType puzzleType = (PuzzleType) puzzleSelect.getValue();
            if (puzzleType == PuzzleType.VERTEX_COLORING || puzzleType == PuzzleType.NONOGRAM) {
                GACState gacState = (GACState) currentNode.getState();
                int unassignedDomainsCounter = 0;
                int violatedConstraintsCounter = 0;
                for (ConstraintInstance constraintInstance : gacState.getConstraintInstances()) {
                    ConstraintValidator validator = constraintInstance.getOriginalConstraint().getConstraintValidator();
                    ArrayList<VariableInstance> variableInstances = constraintInstance.getVariableInstances();
                    int values[] = new int[variableInstances.size()];
                    boolean readyForValidation = true;
                    for (int i = 0; i < variableInstances.size(); i++) {
                        ArrayList<Integer> currentDomain = variableInstances.get(i).getCurrentDomain();
                        if (currentDomain.size() != 1) {
                            readyForValidation = false;
                            unassignedDomainsCounter++;

                        } else {
                            values[i] = currentDomain.get(0);
                        }
                    }
                    if (readyForValidation) {
                        if (!validator.check(values)) {
                            violatedConstraintsCounter++;
                        }
                    }
                }
                violatedConstraintsLabel.setText("Violated constraints: "+violatedConstraintsCounter);
                unassignedDomainsLabel.setText("Non-deduced domains: "+unassignedDomainsCounter);
            }


            int solutionLength = 0;
            while (currentNode != null) {
                currentNode = currentNode.getParent();
                solutionLength++;
            }
            openCountLabel.setText("Open: "+search.getObservableOpenList().size());
            closedCountLabel.setText("Closed: "+search.getObservableClosedList().size());
            solutionLengthLabel.setText("Solution length: "+solutionLength);

        }
    }

    public void showMessage(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("A* Search");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

    private enum PuzzleType {

        NAVIGATION("NavigationPuzzle"),
        VERTEX_COLORING("VertexColoringPuzzle"),
        NONOGRAM("NonogramPuzzle");

        private final String name;

        private PuzzleType(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
