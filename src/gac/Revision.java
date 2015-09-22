package gac;

public class Revision {
    private VariableInstance focalVariable;
    private ConstraintInstance constraintInstance;

    public Revision(VariableInstance focalVariable, ConstraintInstance constraintInstance) {
        this.focalVariable = focalVariable;
        this.constraintInstance = constraintInstance;
    }

    public VariableInstance getFocalVariable() {
        return focalVariable;
    }

    public ConstraintInstance getConstraintInstance() {
        return constraintInstance;
    }
}
