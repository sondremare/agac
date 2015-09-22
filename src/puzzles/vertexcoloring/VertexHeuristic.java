package puzzles.vertexcoloring;

import gac.GACState;
import search.Heuristic;
import search.State;

public class VertexHeuristic implements Heuristic {

    @Override
    public float calculateHeuristicValue(State state) {
        GACState gacState = (GACState) state;

        return 0;
    }
}
