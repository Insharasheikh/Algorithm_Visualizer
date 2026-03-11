package algorithms.graph;

import core.StepController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import ui.CodePane;
import ui.GraphPane;

import java.util.*;

public class TopologicalSort implements GraphAlgorithm {

    @Override
    public String[] getCode() {
        return new String[]{
            "TopologicalSort(graph):",
            "  compute in-degree for all nodes",
            "  queue = all nodes with in-degree 0",
            "  while queue not empty:",
            "    node = queue.dequeue()",
            "    add node to result",
            "    for each neighbor of node:",
            "      in-degree[neighbor]--",
            "      if in-degree[neighbor] == 0:",
            "        queue.enqueue(neighbor)"
        };
    }

    @Override
    public String getTimeComplexity() { return "O(V + E)"; }

    // endNode is ignored for TopologicalSort — no src/dest concept
    @Override
    public void run(List<GraphNode> nodes, List<GraphEdge> edges,
                    GraphNode startNode, GraphNode endNode,
                    GraphPane graphPane, CodePane codePane,
                    int speed, StepController controller) {
        run(nodes, edges, startNode, endNode, graphPane, codePane, speed, controller, null);
    }

    public void run(List<GraphNode> nodes, List<GraphEdge> edges,
                    GraphNode startNode, GraphNode endNode,
                    GraphPane graphPane, CodePane codePane,
                    int speed, StepController controller, Label resultLabel) {
        try {
            Map<Integer, List<GraphEdge>> adj     = buildAdj(edges);
            Map<Integer, GraphNode>       nodeMap = buildNodeMap(nodes);

            Map<Integer, Integer> inDegree = new HashMap<>();
            for (GraphNode n : nodes) inDegree.put(n.id, 0);
            for (GraphEdge e : edges) inDegree.merge(e.to.id, 1, Integer::sum);

            codePane.highlightLine(1);
            sleep(speed, controller);

            Queue<GraphNode> queue = new LinkedList<>();
            for (GraphNode n : nodes) {
                if (inDegree.get(n.id) == 0) queue.add(n);
            }

            codePane.highlightLine(2);
            Set<Integer> visited  = new LinkedHashSet<>();
            Set<Integer> inResult = new LinkedHashSet<>();
            graphPane.updateGraphTopo(nodes, edges, visited, inResult, null, inDegree);
            sleep(speed, controller);

            List<Integer> topoOrder = new ArrayList<>();

            while (!queue.isEmpty()) {
                if (Thread.currentThread().isInterrupted()) return;

                codePane.highlightLine(3);
                sleep(speed / 2, controller);

                GraphNode current = queue.poll();
                visited.add(current.id);

                codePane.highlightLine(4);
                graphPane.updateGraphTopo(nodes, edges, visited, inResult, current, inDegree);
                sleep(speed, controller);

                inResult.add(current.id);
                topoOrder.add(current.id);

                codePane.highlightLine(5);
                sleep(speed / 2, controller);

                for (GraphEdge edge : adj.getOrDefault(current.id, new ArrayList<>())) {
                    if (Thread.currentThread().isInterrupted()) return;

                    GraphNode neighbor = nodeMap.get(edge.to.id);
                    codePane.highlightLine(6);

                    int newDeg = inDegree.get(neighbor.id) - 1;
                    inDegree.put(neighbor.id, newDeg);

                    codePane.highlightLine(7);
                    graphPane.updateGraphTopo(nodes, edges, visited, inResult, neighbor, inDegree);
                    sleep(speed, controller);

                    codePane.highlightLine(8);
                    if (newDeg == 0) {
                        queue.add(neighbor);
                        codePane.highlightLine(9);
                        sleep(speed / 2, controller);
                    }
                }
            }

            graphPane.updateGraphTopo(nodes, edges, visited, inResult, null, inDegree);

            // ── Result output ─────────────────────────────────────────────────
            String resultText;
            if (topoOrder.size() < nodes.size()) {
                resultText = "⚠ Cycle detected — topological sort not possible!";
            } else {
                StringBuilder sb = new StringBuilder("Topo order: ");
                for (int i = 0; i < topoOrder.size(); i++) {
                    sb.append(nodeMap.get(topoOrder.get(i)).label);
                    if (i < topoOrder.size() - 1) sb.append(" → ");
                }
                resultText = sb.toString();
            }

            final String finalResult = resultText;
            if (resultLabel != null) {
                Platform.runLater(() -> resultLabel.setText(finalResult));
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Map<Integer, List<GraphEdge>> buildAdj(List<GraphEdge> edges) {
        Map<Integer, List<GraphEdge>> adj = new HashMap<>();
        for (GraphEdge e : edges) adj.computeIfAbsent(e.from.id, k -> new ArrayList<>()).add(e);
        return adj;
    }

    private Map<Integer, GraphNode> buildNodeMap(List<GraphNode> nodes) {
        Map<Integer, GraphNode> map = new HashMap<>();
        for (GraphNode n : nodes) map.put(n.id, n);
        return map;
    }

    private void sleep(int ms, StepController c) throws InterruptedException {
        c.waitIfPaused();
        Thread.sleep(Math.max(ms, 50));
    }
}