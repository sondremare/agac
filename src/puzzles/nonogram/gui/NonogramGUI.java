package puzzles.nonogram.gui;

import gac.ConstraintInstance;
import gac.GACState;
import gac.Variable;
import gac.VariableInstance;
import gui.GUI;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import puzzles.nonogram.NonogramPuzzle;
import puzzles.vertexcoloring.gui.Vertex;
import search.Search;

public class NonogramGUI implements GUI{
    private NonogramPuzzle puzzle;
    private Pane pane;
    public static double CELL_WIDTH = 10;
    public static double CELL_HEIGHT = 10;
    public static double MARGIN = 30;

    public NonogramGUI(NonogramPuzzle puzzle) {
        this.puzzle = puzzle;
    }

    @Override
    public GridPane initGUI() {
        GridPane root = new GridPane();
        GACState gacState = (GACState) puzzle.getState();
        //clearAll();
        /*pane = new Pane();
        pane.setPrefSize(WIDTH + 2*MARGIN, HEIGHT + 2*MARGIN);
        pane.setStyle("-fx-background-color: white;");
        for (ConstraintInstance constraintInstance : gacState.getConstraintInstances()) {
            Variable startVariable = constraintInstance.getVariableInstances().get(0).getOriginalVariable();
            Variable endVariable = constraintInstance.getVariableInstances().get(1).getOriginalVariable();
            double startX = startVariable.getxPos() + MARGIN;
            double startY = startVariable.getyPos() + MARGIN;
            double endX = endVariable.getxPos() + MARGIN;
            double endY = endVariable.getyPos() + MARGIN;
            Line line = new Line(startX, startY, endX, endY);
            pane.getChildren().add(line);
            lines.add(line);
        }
        for (VariableInstance variableInstance : gacState.getVariableInstances().values()) {
            Vertex vertex = new Vertex(variableInstance, Vertex.getColor(variableInstance.getCurrentDomain()));
            pane.getChildren().add(vertex);
            vertices.put(variableInstance.getIndex(), vertex);
        }*/

        root.add(pane, 0, 0);
        return root;
    }

    @Override
    public void update(Search search) {

    }
}
