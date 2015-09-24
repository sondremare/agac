package puzzles.nonogram;

import gac.GAC;
import gac.GACState;
import puzzles.vertexcoloring.VertexGoalTest;
import puzzles.vertexcoloring.VertexHeuristic;
import puzzles.vertexcoloring.VertexResultFunction;
import search.*;

public class NonogramPuzzle implements Puzzle {

    private GAC gac;
    private GACState gacState;
    private VertexResultFunction vertexResultFunction;
    private VertexHeuristic heuristic;
    private VertexGoalTest goalTest;

    private NonogramPuzzle(GAC gac, GACState gacState) {
        this.gac = gac;
        this.gacState = gacState;
        this.vertexResultFunction = new VertexResultFunction();
        this.heuristic = new VertexHeuristic();
        this.goalTest = new VertexGoalTest();
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
        return null;
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
