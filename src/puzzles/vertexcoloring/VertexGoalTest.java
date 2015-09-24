package puzzles.vertexcoloring;

import gac.ConstraintInstance;
import gac.ConstraintValidator;
import gac.GACState;
import gac.VariableInstance;
import search.GoalTest;
import search.State;

public class VertexGoalTest implements GoalTest{

    @Override
    public boolean isGoalState(State state) {
        int constraintValidatedCounter = 0;

        GACState gacState = (GACState) state;
        for (ConstraintInstance constraintInstance : gacState.getConstraintInstances()) {
            ConstraintValidator validator = constraintInstance.getOriginalConstraint().getConstraintValidator();
            int[] val = new int[2];
            if (validator.check(val)) {
                constraintValidatedCounter++;
            }
        }
        System.out.println(constraintValidatedCounter +"/" +gacState.getConstraintInstances().size());
        for (VariableInstance variableInstance : gacState.getVariableInstances().values()) {
            if (variableInstance.getCurrentDomain().size() != 1) {
                return false;
            }
        }
        return true;
    }
}
