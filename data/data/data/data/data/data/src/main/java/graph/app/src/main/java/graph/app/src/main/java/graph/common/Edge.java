package graph.common;

public class Edge {
    private final String to;
    private final long weight;

    public Edge(String to, long weight) {
        this.to = to;
        this.weight = weight;
    }

    public String getTo() { return to; }
    public long getWeight() { return weight; }
    @Override
    public String toString() { return "->" + to + "(" + weight + ")"; }
}
