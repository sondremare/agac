package puzzles.vertexcoloring.gui;

import gac.VariableInstance;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import puzzles.vertexcoloring.gui.VertexColoringGUI;

import java.util.ArrayList;

public class Vertex extends Circle {

    private static final double RADIUS = 5;

    public Vertex(VariableInstance variableInstance, Color color) {
        super(RADIUS, color);
        double x = variableInstance.getOriginalVariable().getxPos() + VertexColoringGUI.MARGIN;
        double y = variableInstance.getOriginalVariable().getyPos() + VertexColoringGUI.MARGIN;
        setLayoutX(x);
        setLayoutY(y);
    }

    public void resolveColor(VariableInstance variableInstance) {
        setFill(getColor(variableInstance.getCurrentDomain()));
    }

    public static Color getColor(ArrayList<Integer> currentDomain) {
        if (currentDomain.size() > 1) {
            return Color.BLACK;
        }

        int domainValue = currentDomain.get(0);
        switch(domainValue) {
            case 0: return Color.RED;
            case 1: return Color.BLUE;
            case 2: return Color.YELLOW;
            case 3: return Color.GREEN;
            case 4: return Color.ORANGE;
            case 5: return Color.BROWN;
            case 6: return Color.PURPLE;
            case 7: return Color.OLIVEDRAB;
            case 8: return Color.DEEPPINK;
            case 9: return Color.CYAN;
            default: return Color.GREENYELLOW;
        }
    }
}
