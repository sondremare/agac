package gac;

import java.util.ArrayList;

public class Variable {
    private int index;
    private ArrayList<Integer> domain;

    public Variable(int index, ArrayList<Integer> domain) {
        this.index = index;
        this.domain = domain;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Integer> getDomain() {
        return domain;
    }

}
