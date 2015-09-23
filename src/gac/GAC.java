package gac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

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
                ArrayList<VariableInstance> nonFocalVariables = getNonFocalVariables(focalVariable, constraint.getVariableInstances());
                for (VariableInstance variable : nonFocalVariables) {
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
        ArrayList<VariableInstance> nonFocalVariables = getNonFocalVariables(focalVariable, constraint.getVariableInstances());
        Iterator<Integer> iterator = focalVariable.getCurrentDomain().iterator();
        while(iterator.hasNext()) {
            int i = iterator.next();
            values[0] = i;
            if (!checkVariableDomains(validator, values, 0, nonFocalVariables)) {
                iterator.remove();
                revised = true;
            }
        }
        return revised;
    }

        //TODO FIX THIS GENERIC VERSION
   /* public static boolean checkVariableDomains(ConstraintValidator validator, int[] values, int nonFocalVariableCounter, ArrayList<VariableInstance> nonFocalVariables) {
        VariableInstance variable = nonFocalVariables.get(nonFocalVariableCounter);
        int valueIndex = nonFocalVariableCounter + 1;
        boolean constraintValid = false;
        for (int i = 0; i < variable.getCurrentDomain().size(); i++) {
            values[valueIndex] = variable.getCurrentDomain().get(i);
            if (nonFocalVariableCounter == nonFocalVariables.size()-1) { //All variables has assigned domain values
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
    } */

    /** Lame version for just 2 parameters */
    public static boolean checkVariableDomains(ConstraintValidator validator, int[] values, int nonFocalVariableCounter, ArrayList<VariableInstance> nonFocalVariables) {
        VariableInstance variable = nonFocalVariables.get(nonFocalVariableCounter);
        int valueIndex = 1;//nonFocalVariableCounter + 1;
        boolean constraintValid = false;
        for (int i = 0; i < variable.getCurrentDomain().size(); i++) {
            values[1] = variable.getCurrentDomain().get(i);
            if (validator.check(values)) {
                    constraintValid = true;
            }
        }
        return constraintValid;
    }

    public static ArrayList<VariableInstance> getNonFocalVariables(VariableInstance focalVariable, ArrayList<VariableInstance> variables) {
        ArrayList<VariableInstance> nonFocalVariables = new ArrayList<>(variables);
        nonFocalVariables.remove(focalVariable);
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
