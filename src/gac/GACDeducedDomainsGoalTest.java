package gac;

import search.GoalTest;
import search.State;

public class GACDeducedDomainsGoalTest implements GoalTest{

    @Override
    public boolean isGoalState(State state) {
        GACState gacState = (GACState) state;
        for (VariableInstance variableInstance : gacState.getVariableInstances().values()) {
            if (variableInstance.getCurrentDomain().size() != 1) {
                return false;
            }
        }
        return true;
    }
}
