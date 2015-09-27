package puzzles.nonogram.gui;

import gac.VariableInstance;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import puzzles.nonogram.NonogramVariable;

import java.util.ArrayList;

public class Cell extends Rectangle{
    private int row;
    private int col;

    public Cell(int row, int col) {
        super(NonogramGUI.CELL_WIDTH, NonogramGUI.CELL_HEIGHT, Color.GREY);

        this.row = row;
        this.col = col;
        double x = col * NonogramGUI.CELL_WIDTH + NonogramGUI.MARGIN;
        double y = row * NonogramGUI.CELL_HEIGHT + NonogramGUI.MARGIN;
        setLayoutX(x);
        setLayoutY(y);
        setStroke(Color.BLACK);
    }

    public void resolveColor(VariableInstance variableInstance, boolean isRowVariable, int domainLength) {
        Color color = getColor(variableInstance, isRowVariable, domainLength);
        if (color != null) {
            setFill(color);
        }
    }

    public Color getColor(VariableInstance variableInstance, boolean isRowVariable, int domainLength) {
        ArrayList<Integer> currentDomain = variableInstance.getCurrentDomain();
        if (currentDomain.size() > 1) {
            return null;
        }

        int domainValue = currentDomain.get(0);
        String domainString = Integer.toBinaryString(domainValue);
        String[] domainBitArray = addLeadingZeroes(domainString, domainLength).split("");

        if (isRowVariable) {
            if (domainBitArray[col].equals("1")) {
                return Color.RED;
            } else {
                return Color.BLUE;
            }
        } else {
            if (domainBitArray[row].equals("1")) {
                return Color.RED;
            } else {
                return Color.BLUE;
            }
        }
    }

    public String addLeadingZeroes(String val, int n) {
        String allZeroes = new String(new char[n]).replace("\0", "0");
        return (allZeroes + val).substring(val.length());
    }

}
