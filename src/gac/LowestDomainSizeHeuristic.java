package gac;

import search.Heuristic;
import search.State;

public class LowestDomainSizeHeuristic implements Heuristic {

    @Override
    public float calculateHeuristicValue(State state) {
        GACState gacState = (GACState) state;
        int domainSizeCounter = 0;
        for (VariableInstance variableInstance : gacState.getVariableInstances().values()) {
            int domainSize = variableInstance.getCurrentDomain().size();
            if (domainSize > 1) {
                domainSizeCounter += domainSize-1;
            }
        }
        return domainSizeCounter;
    }
}
