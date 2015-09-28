package puzzles.nonogram;

import gac.*;
import search.*;

public class NonogramPuzzle implements Puzzle {

    private GAC gac;
    private GACState gacState;
    private SmallestDomainResultFunction resultfunction;
    private LowestDomainSizeHeuristic heuristic;
    private GACDeducedDomainsGoalTest goalTest;

    public NonogramPuzzle(GAC gac, GACState gacState) {
        this.gac = gac;
        this.gacState = gacState;
        this.resultfunction = new SmallestDomainResultFunction();
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
        return resultfunction;
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
