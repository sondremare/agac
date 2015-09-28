package puzzles.nonogram;

import gac.Variable;

import java.util.ArrayList;

public class NonogramVariable extends Variable {
    private boolean isRowVariable;

    public NonogramVariable(int index, ArrayList<Integer> domain, boolean isRowVariable) {
        super(index, domain);
        this.isRowVariable = isRowVariable;
    }

    public boolean isRowVariable() {
        return isRowVariable;
    }
}
