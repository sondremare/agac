package puzzles.vertexcoloring;

        import gac.GAC;
        import gac.GACState;
        import search.*;

public class VertexColoringPuzzle implements Puzzle{

    private GAC gac;
    private GACState gacState;
    private VertexResultFunction vertexResultFunction;
    private VertexHeuristic vertexHeuristic;
    private VertexGoalTest vertexGoalTest;

    public VertexColoringPuzzle(GAC gac, GACState gacState) {
        this.gac = gac;
        this.gacState = gacState;
        this.vertexResultFunction = new VertexResultFunction();
        this.vertexHeuristic = new VertexHeuristic();
        this.vertexGoalTest = new VertexGoalTest();
    }

    @Override
    public Heuristic getHeuristic() {
        return vertexHeuristic;
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
        return vertexGoalTest;
    }
}
