package puzzles.vertexcoloring;

import gac.Variable;

import java.util.ArrayList;

public class VertexVariable extends Variable {
    private double xPos;
    private double yPos;

    public VertexVariable(int index, ArrayList<Integer> domain, double xPos, double yPos) {
        super(index, domain);
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }
}
