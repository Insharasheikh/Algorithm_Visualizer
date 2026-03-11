package algorithms.graph;

import core.StepController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import ui.CodePane;
import ui.GraphPane;

import java.util.*;

public class BFS implements GraphAlgorithm {

    @Override
    public String[] getCode() {
        return new String[]{
            "BFS(graph, start):",
            "  queue = [start]",
            "  visited = {start}",
            "  while queue not empty:",
            "    node = queue.dequeue()",
            "    for each neighbor of node:",
            "      if neighbor not visited:",
            "        visited.add(neighbor)",
            "        queue.enqueue(neighbor)"
        };
    }

    @Override
    public String getTimeComplexity() { return "O(V + E)"; }

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
            Map<Integer, List<GraphEdge>> adj = buildAdj(nodes, edges);
            Map<Integer, GraphNode> nodeMap   = buildNodeMap(nodes);
            Map<Integer, Integer>   parentMap = new HashMap<>(); // child → parent id

            Set<Integer> visited = new LinkedHashSet<>();
            Queue<GraphNode> queue = new LinkedList<>();

            queue.add(startNode);
            visited.add(startNode.id);
            parentMap.put(startNode.id, -1);

            codePane.highlightLine(1);
            graphPane.updateGraph(nodes, edges, visited, new HashSet<>(), null, null);
            sleep(speed, controller);

            codePane.highlightLine(2);
            sleep(speed / 2, controller);

            List<Integer> traversalOrder = new ArrayList<>();
            traversalOrder.add(startNode.id);

            while (!queue.isEmpty()) {
                if (Thread.currentThread().isInterrupted()) return;

                codePane.highlightLine(3);
                sleep(speed / 2, controller);

                GraphNode current = queue.poll();

                codePane.highlightLine(4);
                graphPane.updateGraph(nodes, edges, visited, new HashSet<>(), current, null);
                sleep(speed, controller);

                for (GraphEdge edge : adj.getOrDefault(current.id, new ArrayList<>())) {
                    if (Thread.currentThread().isInterrupted()) return;

                    GraphNode neighbor = nodeMap.get(edge.to.id);
                    codePane.highlightLine(5);
                    graphPane.updateGraph(nodes, edges, visited, new HashSet<>(), current, neighbor);
                    sleep(speed, controller);

                    codePane.highlightLine(6);
                    if (!visited.contains(neighbor.id)) {
                        visited.add(neighbor.id);
                        queue.add(neighbor);
                        parentMap.put(neighbor.id, current.id);
                        traversalOrder.add(neighbor.id);

                        codePane.highlightLine(7);
                        graphPane.updateGraph(nodes, edges, visited, new HashSet<>(), current, neighbor);
                        sleep(speed, controller);

                        codePane.highlightLine(8);
                        sleep(speed / 2, controller);
                    }
                }
            }

            // ── Build path if endNode given ───────────────────────────────────
            Set<Integer> pathIds  = new HashSet<>();
            Set<String>  pathEdgeKeys = new HashSet<>();
            String resultText;

            if (endNode != null && parentMap.containsKey(endNode.id)) {
                // Reconstruct path from end → start
                List<Integer> path = new ArrayList<>();
                int cur = endNode.id;
                while (cur != -1) {
                    path.add(cur);
                    cur = parentMap.getOrDefault(cur, -1);
                }
                Collections.reverse(path);
                pathIds.addAll(path);
                for (int i = 0; i < path.size() - 1; i++) {
                    pathEdgeKeys.add(path.get(i) + "-" + path.get(i + 1));
                    pathEdgeKeys.add(path.get(i + 1) + "-" + path.get(i)); // undirected
                }

                // Build label string e.g. "A → B → C → D"
                StringBuilder sb = new StringBuilder("Path: ");
                for (int i = 0; i < path.size(); i++) {
                    sb.append(nodeMap.get(path.get(i)).label);
                    if (i < path.size() - 1) sb.append(" → ");
                }
                sb.append("  (").append(path.size() - 1).append(" hops)");
                resultText = sb.toString();
            } else if (endNode != null) {
                resultText = "✘ No path from " + startNode.label + " to " + endNode.label;
            } else {
                // No dest — show traversal order
                StringBuilder sb = new StringBuilder("BFS order: ");
                for (int i = 0; i < traversalOrder.size(); i++) {
                    sb.append(nodeMap.get(traversalOrder.get(i)).label);
                    if (i < traversalOrder.size() - 1) sb.append(" → ");
                }
                resultText = sb.toString();
            }

            // Final draw — highlight path edges + nodes
            graphPane.updateGraphWithPath(nodes, edges, visited, pathIds, pathEdgeKeys, null, null);

            final String finalResult = resultText;
            if (resultLabel != null) {
                Platform.runLater(() -> resultLabel.setText(finalResult));
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Map<Integer, List<GraphEdge>> buildAdj(List<GraphNode> nodes, List<GraphEdge> edges) {
        Map<Integer, List<GraphEdge>> adj = new HashMap<>();
        for (GraphNode n : nodes) adj.put(n.id, new ArrayList<>());
        for (GraphEdge e : edges) {
            adj.get(e.from.id).add(e);
            adj.get(e.to.id).add(new GraphEdge(e.to, e.from, e.weight));
        }
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