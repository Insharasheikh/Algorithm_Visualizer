package algorithms.graph;

import core.StepController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import ui.CodePane;
import ui.GraphPane;

import java.util.*;

public class Dijkstra implements GraphAlgorithm {

    @Override
    public String[] getCode() {
        return new String[]{
            "Dijkstra(graph, start):",
            "  dist[start] = 0, dist[others] = ∞",
            "  priority queue = {start}",
            "  while queue not empty:",
            "    u = node with min dist",
            "    for each neighbor v of u:",
            "      newDist = dist[u] + weight(u,v)",
            "      if newDist < dist[v]:",
            "        dist[v] = newDist"
        };
    }

    @Override
    public String getTimeComplexity() { return "O((V + E) log V)"; }

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
            Map<Integer, List<GraphEdge>> adj     = buildAdj(nodes, edges);
            Map<Integer, GraphNode>       nodeMap = buildNodeMap(nodes);
            Map<Integer, Integer>         dist    = new HashMap<>();
            Map<Integer, Integer>         parent  = new HashMap<>(); // child → parent id
            Set<Integer>                  visited = new LinkedHashSet<>();

            for (GraphNode n : nodes) dist.put(n.id, Integer.MAX_VALUE);
            dist.put(startNode.id, 0);
            parent.put(startNode.id, -1);

            codePane.highlightLine(1);
            graphPane.updateGraphDijkstra(nodes, edges, visited, dist, startNode, null);
            sleep(speed, controller);

            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            pq.offer(new int[]{0, startNode.id});

            codePane.highlightLine(2);
            sleep(speed / 2, controller);

            while (!pq.isEmpty()) {
                if (Thread.currentThread().isInterrupted()) return;

                codePane.highlightLine(3);
                sleep(speed / 2, controller);

                int[] curr     = pq.poll();
                int currDist   = curr[0];
                int currId     = curr[1];

                if (visited.contains(currId)) continue;
                visited.add(currId);
                GraphNode currNode = nodeMap.get(currId);

                codePane.highlightLine(4);
                graphPane.updateGraphDijkstra(nodes, edges, visited, dist, currNode, null);
                sleep(speed, controller);

                for (GraphEdge edge : adj.getOrDefault(currId, new ArrayList<>())) {
                    if (Thread.currentThread().isInterrupted()) return;

                    GraphNode neighbor = nodeMap.get(edge.to.id);
                    int newDist = currDist + edge.weight;

                    codePane.highlightLine(5);
                    graphPane.updateGraphDijkstra(nodes, edges, visited, dist, currNode, neighbor);
                    sleep(speed, controller);

                    codePane.highlightLine(6);
                    if (newDist < dist.get(neighbor.id)) {
                        dist.put(neighbor.id, newDist);
                        parent.put(neighbor.id, currId);
                        pq.offer(new int[]{newDist, neighbor.id});

                        codePane.highlightLine(7);
                        graphPane.updateGraphDijkstra(nodes, edges, visited, dist, currNode, neighbor);
                        sleep(speed, controller);

                        codePane.highlightLine(8);
                        sleep(speed / 2, controller);
                    }
                }
            }

            // ── Build shortest path to endNode ────────────────────────────────
            Set<Integer> pathIds      = new HashSet<>();
            Set<String>  pathEdgeKeys = new HashSet<>();
            String resultText;

            if (endNode != null && dist.get(endNode.id) != Integer.MAX_VALUE) {
                List<Integer> path = new ArrayList<>();
                int cur = endNode.id;
                while (cur != -1) {
                    path.add(cur);
                    cur = parent.getOrDefault(cur, -1);
                }
                Collections.reverse(path);
                pathIds.addAll(path);
                for (int i = 0; i < path.size() - 1; i++) {
                    // Dijkstra is directed — only add forward direction
                    pathEdgeKeys.add(path.get(i) + "-" + path.get(i + 1));
                }

                StringBuilder sb = new StringBuilder("Shortest path: ");
                for (int i = 0; i < path.size(); i++) {
                    sb.append(nodeMap.get(path.get(i)).label);
                    if (i < path.size() - 1) sb.append(" → ");
                }
                sb.append("  |  Total cost: ").append(dist.get(endNode.id));
                resultText = sb.toString();

            } else if (endNode != null) {
                resultText = "✘ No path from " + startNode.label + " to " + endNode.label;
            } else {
                // No dest — show all distances from start
                StringBuilder sb = new StringBuilder("Distances from " + startNode.label + ":  ");
                List<GraphNode> sorted = new ArrayList<>(nodes);
                sorted.sort(Comparator.comparingInt(n -> n.id));
                for (GraphNode n : sorted) {
                    int d = dist.getOrDefault(n.id, Integer.MAX_VALUE);
                    sb.append(n.label).append("=").append(d == Integer.MAX_VALUE ? "∞" : d).append("  ");
                }
                resultText = sb.toString().trim();
            }

            // Final draw — highlight path
            graphPane.updateGraphDijkstraWithPath(nodes, edges, visited, dist, pathIds, pathEdgeKeys, null, null);

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
        // Dijkstra is directed — only forward edges
        for (GraphEdge e : edges) adj.get(e.from.id).add(e);
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