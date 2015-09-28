package puzzles.nonogram.gui;

import gac.GACState;
import gac.VariableInstance;
import gui.GUI;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import puzzles.nonogram.NonogramPuzzle;
import puzzles.nonogram.NonogramVariable;
import search.Node;
import search.Puzzle;
import search.Search;

public class NonogramGUI implements GUI{
    private NonogramPuzzle puzzle;
    private Pane pane;
    private Cell[][] cells;
    private int rowCounter;
    private int colCounter;
    public static double CELL_WIDTH = 30;
    public static double CELL_HEIGHT = 30;
    public static double MARGIN = 30;


    public NonogramGUI(Puzzle puzzle) {
        this.puzzle = (NonogramPuzzle)puzzle;
    }

    @Override
    public GridPane initGUI() {
        rowCounter = 0;
        colCounter = 0;
        GridPane root = new GridPane();
        GACState gacState = (GACState) puzzle.getState();
        for (VariableInstance variableInstance : gacState.getVariableInstances().values()) {
            if (((NonogramVariable)variableInstance.getOriginalVariable()).isRowVariable()) {
                rowCounter++;
                variableInstance.getCurrentDomain().get(0);
            } else {
                colCounter++;
            }
        }

        pane = new Pane();
        pane.setPrefSize(colCounter * CELL_WIDTH + 2*MARGIN, rowCounter * CELL_HEIGHT + 2*MARGIN);
        pane.setStyle("-fx-background-color: white;");

        cells = new Cell[rowCounter][colCounter];
        for (int i = 0; i < rowCounter; i++) {
            for (int j = 0; j < colCounter; j++) {
                Cell cell = new Cell(i, j);
                pane.getChildren().add(cell);
                cells[i][j] = cell;
            }
        }

        root.add(pane, 0, 0);
        return root;
    }

    @Override
    public void update(Search search) {
        Node currentNode = search.getCurrentNode();
        if (currentNode != null) {
            GACState gacState = (GACState) currentNode.getState();
            for (VariableInstance variableInstance : gacState.getVariableInstances().values()) {
                int index = variableInstance.getIndex();
                boolean isRowVariable = ((NonogramVariable)variableInstance.getOriginalVariable()).isRowVariable();
                if (isRowVariable) {
                    for (int i = 0; i < colCounter; i++) {
                        cells[index][i].resolveColor(variableInstance, isRowVariable, colCounter);
                    }
                } else {
                    for (int j = 0; j < rowCounter; j++) {
                        cells[j][index-rowCounter].resolveColor(variableInstance, isRowVariable, rowCounter);
                    }
                }
            }
        }

    }

    public void clearAll() {
        if (cells != null) {
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    pane.getChildren().remove(cells[i][j]);
                }

            }
        }
    }
}
