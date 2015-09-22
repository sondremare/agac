package gac;

import java.util.ArrayList;

public class VariableInstance {
    private int index;
    private Variable originalVariable;
    private ArrayList<Integer> currentDomain;

    public VariableInstance(int index, Variable originalVariable, ArrayList<Integer> currentDomain) {
        this.index = index;
        this.originalVariable = originalVariable;
        this.currentDomain = currentDomain;
    }

    public int getIndex() {
        return index;
    }

    public Variable getOriginalVariable() {
        return originalVariable;
    }

    public ArrayList<Integer> getCurrentDomain() {
        return currentDomain;
    }

    public void setCurrentDomain(ArrayList<Integer> currentDomain) {
        this.currentDomain = currentDomain;
    }
}
