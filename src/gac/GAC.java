package gac;

import java.util.*;

public class GAC {
    private HashMap<Integer, Variable> variables;
    private ArrayList<Constraint> constraints;

    public GAC(HashMap<Integer, Variable> variables, ArrayList<Constraint> constraints) {
        this.variables = variables;
        this.constraints = constraints;
    }

    public HashMap<Integer, Variable> getVariables() {
        return variables;
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public static void initialize(GACState gac) {
        ArrayList<ConstraintInstance> constraints = gac.getConstraintInstances();
        for (ConstraintInstance constraint : constraints) {
            for (VariableInstance variable : constraint.getVariableInstances()) {
                gac.getReviseQueue().add(new Revision(variable, constraint));
            }
        }
    }

    public static void domainFiltering(GACState gac) {
        ListIterator<Revision> iterator = gac.getReviseQueue().listIterator();
        while (iterator.hasNext()) {
            Revision revision = iterator.next();
            iterator.remove();
            if (revise(revision) ) {
                for (Revision newRevision : getAffectedRevisions(gac, revision, true)) {
                    iterator.add(newRevision);
                    iterator.previous();
                }
            }
        }
    }

    public static void rerun(GACState gac, VariableInstance focalVariable) {
        Revision rev = new Revision(focalVariable, null);
        gac.getReviseQueue().addAll(getAffectedRevisions(gac, rev, false));
        domainFiltering(gac);
    }

    private static ArrayList<Revision> getAffectedRevisions(GACState gac, Revision revision, boolean excludeCurrentConstraint) {
        ArrayList<Revision> affectedRevisions = new ArrayList<>();
        ArrayList<ConstraintInstance> constraints = new ArrayList<>(gac.getConstraintInstances());
        if (excludeCurrentConstraint) {
            constraints.remove(revision.getConstraintInstance());
        }
        VariableInstance focalVariable = revision.getFocalVariable();
        for (ConstraintInstance constraint : constraints) {
            if (constraint.getVariableInstances().contains(focalVariable)) {
                HashMap<Integer, VariableInstance> nonFocalVariables = getNonFocalVariables(focalVariable, constraint.getVariableInstances());
                for (VariableInstance variable : nonFocalVariables.values()) {
                    affectedRevisions.add(new Revision(variable, constraint));
                }
            }
        }
        return affectedRevisions;
    }

    public static boolean revise(Revision revision) {
        boolean revised = false;
        VariableInstance focalVariable = revision.getFocalVariable();
        ConstraintInstance constraint = revision.getConstraintInstance();
        ConstraintValidator validator = constraint.getOriginalConstraint().getConstraintValidator();
        int[] values = new int[constraint.getVariableInstances().size()];
        HashMap<Integer, VariableInstance> nonFocalVariables = getNonFocalVariables(focalVariable, constraint.getVariableInstances());
        Iterator<Integer> iterator = focalVariable.getCurrentDomain().iterator();
        int focalVariableIndex = constraint.getVariableInstances().indexOf(focalVariable);
        while(iterator.hasNext()) {
            HashMap<Integer, VariableInstance> nonFocalVariablesCopy = new HashMap<Integer, VariableInstance>(nonFocalVariables);
            int i = iterator.next();
            values[focalVariableIndex] = i;
            if (!checkVariableDomains(validator, values, 0, nonFocalVariablesCopy)) {
                iterator.remove();
                revised = true;
            }
        }
        return revised;
    }

    public static boolean checkVariableDomains(ConstraintValidator validator, int[] values, int nonFocalVariableCounter, HashMap<Integer, VariableInstance> nonFocalVariables) {
        Iterator iterator = nonFocalVariables.entrySet().iterator();
        Map.Entry<Integer, VariableInstance> pair = (Map.Entry) iterator.next();
        iterator.remove();
        VariableInstance variable = pair.getValue();
        boolean constraintValid = false;
        for (int i = 0; i < variable.getCurrentDomain().size(); i++) {
            values[pair.getKey()] = variable.getCurrentDomain().get(i);
            if (!iterator.hasNext()) { //All variables has assigned domain values
                if (validator.check(values)) {
                    return true;
                }
            } else {
                if (checkVariableDomains(validator, values, nonFocalVariableCounter++, nonFocalVariables)) {
                    constraintValid = true;
                }
            }
        }
        return constraintValid;
    }

    public static HashMap<Integer, VariableInstance> getNonFocalVariables(VariableInstance focalVariable, ArrayList<VariableInstance> variables) {
        HashMap<Integer, VariableInstance> nonFocalVariables = new HashMap<>();
        int focalIndex = variables.indexOf(focalVariable);
        for (int i = 0; i < variables.size(); i++) {
            nonFocalVariables.put(i, variables.get(i));
        }
        nonFocalVariables.remove(focalIndex);
        return nonFocalVariables;
    }

    public static boolean hasEmptyDomain(GACState gacState) {
        for (VariableInstance variableInstance : gacState.getVariableInstances().values()) {
            if (variableInstance.getCurrentDomain().size() == 0) {
                return true;
            }
        }
        return false;
    }
}
