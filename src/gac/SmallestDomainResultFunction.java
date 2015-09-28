package gac;

import search.Action;
import search.ResultFunction;
import search.State;

import java.util.*;

public class SmallestDomainResultFunction implements ResultFunction {
    private static final int DEDUCED = 1;
    @Override
    public ArrayList<State> result(ArrayList<Action> actions, State state) {
        GACState gacState = (GACState) state;
        HashMap<Integer, VariableInstance> variables = gacState.getVariableInstances();
        VariableInstance variableWithSmallestDomain = findVariableWithSmallestDomain(variables);
        ArrayList<State> newStates = new ArrayList<>();
        for (int i : variableWithSmallestDomain.getCurrentDomain()) {
            GACState childState = new GACState(gacState);
            ArrayList<Integer> newDomain = new ArrayList<>();
            newDomain.add(i);
            VariableInstance variableInstance = childState.getVariableInstances().get(variableWithSmallestDomain.getIndex());
            variableInstance.setCurrentDomain(newDomain);
            childState.getVariableInstances().put(variableWithSmallestDomain.getIndex(), variableInstance);
            GAC.rerun(childState, variableInstance);
            if (!GAC.hasEmptyDomain(childState)) {
                newStates.add(childState);
            }
        }
        return newStates;
    }

    public VariableInstance findVariableWithSmallestDomain(HashMap<Integer, VariableInstance> variables) {
        Comparator<VariableInstance> smallestDomain = new Comparator<VariableInstance>() {
            @Override
            public int compare(VariableInstance v1, VariableInstance v2) {
                if (v1.getCurrentDomain().size() == DEDUCED) {
                    if (v2.getCurrentDomain().size() == DEDUCED) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else if (v2.getCurrentDomain().size() == DEDUCED) {
                    return -1;
                } else if (v1.getCurrentDomain().size() < v2.getCurrentDomain().size()) {
                    return -1;
                } else if (v1.getCurrentDomain().size() > v2.getCurrentDomain().size()) {
                    return 1;
                }
                else return 0;
            }
        };

        List<VariableInstance> variableList = new ArrayList<VariableInstance>(variables.values());
        Collections.sort(variableList, smallestDomain);
        return variableList.get(0);
    }

}
