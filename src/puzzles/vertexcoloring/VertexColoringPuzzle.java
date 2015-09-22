package puzzles.vertexcoloring;

        import gac.GAC;
        import gac.GACState;
        import search.*;

public class VertexColoringPuzzle implements Puzzle{

    private GAC gac;
    private GACState gacState;
    private VertexResultFunction vertexResultFunction;

    public VertexColoringPuzzle(GAC gac, GACState gacState) {
        this.gac = gac;
        this.gacState = gacState;
        this.vertexResultFunction = new VertexResultFunction();
    }

    @Override
    public Heuristic getHeuristic() {
        return null;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public ResultFunction getResultFunction() {
        return vertexResultFunction;
    }

    @Override
    public State getState() {
        return gacState;
    }

    @Override
    public GoalTest getGoalTest() {
        return null;
    }
}
