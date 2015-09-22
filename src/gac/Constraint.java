package gac;

import java.util.ArrayList;

public class Constraint {
    private ArrayList<Variable> variables;
    private ConstraintValidator constraintValidator;

    public Constraint(ArrayList<Variable> variables, ConstraintValidator constraintValidator) {
        this.variables = variables;
        this.constraintValidator = constraintValidator;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public ConstraintValidator getConstraintValidator() {
        return constraintValidator;
    }
}
