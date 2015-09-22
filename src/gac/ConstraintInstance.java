package gac;

import java.util.ArrayList;

public class ConstraintInstance {
    private Constraint originalConstraint;
    private ArrayList<VariableInstance> variables;

    public ConstraintInstance(Constraint originalConstraint, ArrayList<VariableInstance> variables) {
        this.originalConstraint = originalConstraint;
        this.variables = variables;
    }

    public Constraint getOriginalConstraint() {
        return originalConstraint;
    }

    public ArrayList<VariableInstance> getVariableInstances() {
        return variables;
    }
}
