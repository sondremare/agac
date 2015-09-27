package gac;

import search.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GACState implements State {

    private HashMap<Integer, VariableInstance> variableInstances;
    private ArrayList<ConstraintInstance> constraintInstances;
    private ArrayList<Revision> reviseQueue = new ArrayList<>();

    public GACState(GAC gac) {
        this.variableInstances = new HashMap<>();
        this.constraintInstances = new ArrayList<>();

        for (Map.Entry<Integer, Variable> entry : gac.getVariables().entrySet()) {
            Variable variable = entry.getValue();
            int index = variable.getIndex();
            variableInstances.put(index, new VariableInstance(index, variable, variable.getDomain()));
        }
        for (Constraint constraint : gac.getConstraints()) {
            ArrayList<VariableInstance> constraintVariableInstances = new ArrayList<>();
            for (Variable constraintVariable : constraint.getVariables()) {
                constraintVariableInstances.add(variableInstances.get(constraintVariable.getIndex()));
            }
            constraintInstances.add(new ConstraintInstance(constraint, constraintVariableInstances));
        }
    }

    public GACState(GACState gacState) {
        this.variableInstances = new HashMap<>();
        this.constraintInstances = new ArrayList<>();

        for (Map.Entry<Integer, VariableInstance> entry : gacState.getVariableInstances().entrySet()) {
            VariableInstance variable = entry.getValue();
            int index = variable.getIndex();
            ArrayList<Integer> currentDomain = (ArrayList<Integer>) variable.getCurrentDomain().clone();
            variableInstances.put(index, new VariableInstance(index, variable.getOriginalVariable(), currentDomain));
        }
        for (ConstraintInstance constraint : gacState.getConstraintInstances()) {
            ArrayList<VariableInstance> constraintVariableInstances = new ArrayList<>();
            for (VariableInstance variable : constraint.getVariableInstances()) {
                constraintVariableInstances.add(variableInstances.get(variable.getIndex()));
            }
            constraintInstances.add(new ConstraintInstance(constraint.getOriginalConstraint(), constraintVariableInstances));
        }
    }

    public HashMap<Integer, VariableInstance> getVariableInstances() {
        return variableInstances;
    }

    public ArrayList<ConstraintInstance> getConstraintInstances() {
        return constraintInstances;
    }

    public ArrayList<Revision> getReviseQueue() {
        return reviseQueue;
    }

    @Override
    public void initialize() {
        GAC.initialize(this);
        GAC.domainFiltering(this);
    }

    @Override
    public float getGCost() {
        return 1;
    }

    @Override
    public boolean equals(State state) {
        GACState gacState = (GACState) state;
        for (int i = 0; i < variableInstances.size(); i++) {
            ArrayList<Integer> currentDomain = variableInstances.get(i).getCurrentDomain();
            ArrayList<Integer> otherDomain = gacState.getVariableInstances().get(i).getCurrentDomain();
            if (currentDomain.size() == otherDomain.size()) {
                for (int j = 0; j < currentDomain.size(); j++) {
                    if (currentDomain.get(j) != otherDomain.get(j)) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
