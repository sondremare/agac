package gac;

import java.util.ArrayList;

public class Variable {
    private int index;
    private ArrayList<Integer> domain;
    private double xPos;
    private double yPos;

    public Variable(int index, ArrayList<Integer> domain, double xPos, double yPos) {
        this.index = index;
        this.domain = domain;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Integer> getDomain() {
        return domain;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }
}
