package search;

public interface State {
    abstract void initialize();
    abstract float getGCost();
    abstract boolean equals(State state);
}
