package search;

import java.util.ArrayList;

public class BreadthFirstSearch extends Search{

    public BreadthFirstSearch(Puzzle puzzle) {
        super(puzzle);
    }

    @Override
    public Node popNode(ArrayList<Node> nodes) {
        return nodes.get(0);
    }
}
