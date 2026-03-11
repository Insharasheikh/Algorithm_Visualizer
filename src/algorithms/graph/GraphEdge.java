package algorithms.graph;

/**
 * Represents a directed/undirected edge between two nodes.
 * Weight is mutable so user can change it before running algorithm.
 */
public class GraphEdge {
    public final GraphNode from;
    public final GraphNode to;
    public int weight; // mutable — user can change before running

    public GraphEdge(GraphNode from, GraphNode to, int weight) {
        this.from   = from;
        this.to     = to;
        this.weight = weight;
    }

    public GraphEdge(GraphNode from, GraphNode to) {
        this(from, to, 1); // default weight = 1
    }
}