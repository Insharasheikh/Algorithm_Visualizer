package algorithms.graph;

import core.StepController;
import java.util.List;
import ui.CodePane;
import ui.GraphPane;

public interface GraphAlgorithm {
    String[] getCode();
    String getTimeComplexity();

    /**
     * @param startNode start node (all algos)
     * @param endNode   destination node for path highlighting (null for TopologicalSort)
     */
    void run(List<GraphNode> nodes, List<GraphEdge> edges,
             GraphNode startNode, GraphNode endNode,
             GraphPane graphPane, CodePane codePane,
             int speed, StepController controller);
}