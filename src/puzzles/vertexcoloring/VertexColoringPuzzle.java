package puzzles.vertexcoloring;

import gac.*;
import search.*;

public class VertexColoringPuzzle implements Puzzle{

    private GAC gac;
    private GACState gacState;
    private SmallestDomainResultFunction resultFunction;
    private LowestDomainSizeHeuristic heuristic;
    private GACDeducedDomainsGoalTest goalTest;

    public VertexColoringPuzzle(GAC gac, GACState gacState) {
        this.gac = gac;
        this.gacState = gacState;
        this.resultFunction = new SmallestDomainResultFunction();
        this.heuristic = new LowestDomainSizeHeuristic();
        this.goalTest = new GACDeducedDomainsGoalTest();
    }

    @Override
    public Heuristic getHeuristic() {
        return heuristic;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public ResultFunction getResultFunction() {
        return resultFunction;
    }

    @Override
    public State getState() {
        return gacState;
    }

    @Override
    public GoalTest getGoalTest() {
        return goalTest;
    }
}
