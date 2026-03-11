package algorithms.graph;

/**
 * Represents a node in the drawn graph.
 */
public class GraphNode {
    public final int id;
    public String label;
    public double x, y; // position on canvas

    public GraphNode(int id, String label, double x, double y) {
        this.id    = id;
        this.label = label;
        this.x     = x;
        this.y     = y;
    }
}