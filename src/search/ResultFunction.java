package search;

import java.util.ArrayList;

public interface ResultFunction {

    abstract ArrayList<State> result(ArrayList<Action> actions, State state);
}
