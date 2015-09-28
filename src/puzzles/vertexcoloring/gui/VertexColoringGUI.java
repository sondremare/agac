package puzzles.vertexcoloring.gui;

import gac.ConstraintInstance;
import gac.GACState;
import gac.VariableInstance;
import gui.GUI;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import puzzles.vertexcoloring.VertexColoringPuzzle;
import puzzles.vertexcoloring.VertexVariable;
import search.Node;
import search.Puzzle;
import search.Search;

import java.util.ArrayList;
import java.util.HashMap;

public class VertexColoringGUI implements GUI {

    private VertexColoringPuzzle puzzle;
    private HashMap<Integer, Vertex> vertices = new HashMap<>();
    private ArrayList<Line> lines = new ArrayList<>();
    private Pane pane;
    public static double WIDTH = 1000.0;
    public static double HEIGHT = 700.0;
    public static double MARGIN = 30;

    public VertexColoringGUI(Puzzle puzzle) {
        this.puzzle = (VertexColoringPuzzle) puzzle;
    }
    @Override
    public GridPane initGUI() {
        GridPane root = new GridPane();
        GACState gacState = (GACState) puzzle.getState();
        clearAll();
        pane = new Pane();
        pane.setPrefSize(WIDTH + 2*MARGIN, HEIGHT + 2*MARGIN);
        pane.setStyle("-fx-background-color: white;");
        for (ConstraintInstance constraintInstance : gacState.getConstraintInstances()) {
            VertexVariable startVariable = (VertexVariable)constraintInstance.getVariableInstances().get(0).getOriginalVariable();
            VertexVariable endVariable = (VertexVariable)constraintInstance.getVariableInstances().get(1).getOriginalVariable();
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
                vertices.get(variableInstance.getIndex()).resolveColor(variableInstance);
            }
        }
    }

    public void clearAll() {
        for (Vertex vertex : vertices.values()) {
            pane.getChildren().remove(vertex);
        }
        for (Line line : lines) {
            pane.getChildren().remove(line);
        }
    }
}
