package ui;

import algorithms.graph.GraphEdge;
import algorithms.graph.GraphNode;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.*;

public class GraphPane extends Pane {

    private final List<GraphNode> nodes = new ArrayList<>();
    private final List<GraphEdge> edges = new ArrayList<>();
    private int nextId = 0;

    private GraphNode selectedNode = null;
    private boolean   isDirected   = false;

    // Src/dest — shown with colored rings, set from Main before run
    private GraphNode srcNode  = null;
    private GraphNode destNode = null;

    private final Canvas canvas;
    private static final double NODE_RADIUS = 22;

    private Set<Integer>          visitedIds   = new HashSet<>();
    private Set<Integer>          resultIds    = new HashSet<>();
    private Set<Integer>          pathIds      = new HashSet<>();
    private Set<String>           pathEdgeKeys = new HashSet<>();
    private GraphNode             activeNode   = null;
    private GraphNode             examineNode  = null;
    private Map<Integer, Integer> distMap      = null;
    private Map<Integer, Integer> inDegreeMap  = null;

    public GraphPane() {
        canvas = new Canvas(800, 400);
        this.getChildren().add(canvas);
        this.setStyle("-fx-background-color: #1e1e2e;");
        this.widthProperty().addListener((obs, o, n)  -> { canvas.setWidth(n.doubleValue());  redraw(); });
        this.heightProperty().addListener((obs, o, n) -> { canvas.setHeight(n.doubleValue()); redraw(); });
        setupMouseHandlers();
        redraw();
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public List<GraphNode> getNodes() { return nodes; }
    public List<GraphEdge> getEdges() { return edges; }
    public void setDirected(boolean d) { this.isDirected = d; redraw(); }

    public void setSrcDest(GraphNode src, GraphNode dest) {
        this.srcNode  = src;
        this.destNode = dest;
        redraw();
    }

    public void clearGraph() {
        nodes.clear(); edges.clear();
        nextId = 0; selectedNode = null;
        srcNode = null; destNode = null;
        resetVisualization(); redraw();
    }

    public void updateGraph(List<GraphNode> n, List<GraphEdge> e,
                            Set<Integer> visited, Set<Integer> result,
                            GraphNode active, GraphNode examine) {
        Platform.runLater(() -> {
            visitedIds = new HashSet<>(visited); resultIds = new HashSet<>(result);
            pathIds = new HashSet<>(); pathEdgeKeys = new HashSet<>();
            activeNode = active; examineNode = examine;
            distMap = null; inDegreeMap = null;
            redraw();
        });
    }

    /** BFS/DFS final — highlights path nodes + edges green */
    public void updateGraphWithPath(List<GraphNode> n, List<GraphEdge> e,
                                    Set<Integer> visited, Set<Integer> pNodes,
                                    Set<String> pEdges, GraphNode active, GraphNode examine) {
        Platform.runLater(() -> {
            visitedIds = new HashSet<>(visited); resultIds = new HashSet<>();
            pathIds = new HashSet<>(pNodes); pathEdgeKeys = new HashSet<>(pEdges);
            activeNode = active; examineNode = examine;
            distMap = null; inDegreeMap = null;
            redraw();
        });
    }

    public void updateGraphDijkstra(List<GraphNode> n, List<GraphEdge> e,
                                    Set<Integer> visited, Map<Integer, Integer> dist,
                                    GraphNode active, GraphNode examine) {
        Platform.runLater(() -> {
            visitedIds = new HashSet<>(visited); distMap = new HashMap<>(dist);
            pathIds = new HashSet<>(); pathEdgeKeys = new HashSet<>();
            activeNode = active; examineNode = examine; inDegreeMap = null;
            redraw();
        });
    }

    /** Dijkstra final — highlights shortest path green */
    public void updateGraphDijkstraWithPath(List<GraphNode> n, List<GraphEdge> e,
                                            Set<Integer> visited, Map<Integer, Integer> dist,
                                            Set<Integer> pNodes, Set<String> pEdges,
                                            GraphNode active, GraphNode examine) {
        Platform.runLater(() -> {
            visitedIds = new HashSet<>(visited); distMap = new HashMap<>(dist);
            pathIds = new HashSet<>(pNodes); pathEdgeKeys = new HashSet<>(pEdges);
            activeNode = active; examineNode = examine; inDegreeMap = null;
            redraw();
        });
    }

    public void updateGraphTopo(List<GraphNode> n, List<GraphEdge> e,
                                Set<Integer> visited, Set<Integer> result,
                                GraphNode active, Map<Integer, Integer> inDegree) {
        Platform.runLater(() -> {
            visitedIds = new HashSet<>(visited); resultIds = new HashSet<>(result);
            pathIds = new HashSet<>(); pathEdgeKeys = new HashSet<>();
            activeNode = active;
            inDegreeMap = inDegree != null ? new HashMap<>(inDegree) : null;
            distMap = null;
            redraw();
        });
    }

    public void resetVisualization() {
        visitedIds = new HashSet<>(); resultIds = new HashSet<>();
        pathIds = new HashSet<>(); pathEdgeKeys = new HashSet<>();
        activeNode = null; examineNode = null;
        distMap = null; inDegreeMap = null;
    }

    // ── Mouse ─────────────────────────────────────────────────────────────────

    private void setupMouseHandlers() {
        canvas.setOnMouseClicked(e -> {
            double mx = e.getX(), my = e.getY();
            if (e.getButton() == MouseButton.PRIMARY) {
                GraphNode hitNode = nodeAt(mx, my);
                GraphEdge hitEdge = hitNode == null ? nearestEdge(mx, my) : null;

                if (hitNode == null && hitEdge == null) {
                    nodes.add(new GraphNode(nextId, String.valueOf((char)('A' + nextId % 26)), mx, my));
                    nextId++; selectedNode = null; redraw();
                } else if (hitEdge != null) {
                    editEdgeWeight(hitEdge);
                } else if (selectedNode == null) {
                    selectedNode = hitNode; redraw();
                } else if (selectedNode == hitNode) {
                    selectedNode = null; redraw();
                } else {
                    boolean exists = edges.stream().anyMatch(ed ->
                        ed.from.id == selectedNode.id && ed.to.id == hitNode.id);
                    if (!exists) edges.add(new GraphEdge(selectedNode, hitNode, 1));
                    selectedNode = null; redraw();
                }
            } else if (e.getButton() == MouseButton.SECONDARY) {
                GraphNode hitNode = nodeAt(mx, my);
                if (hitNode != null) {
                    nodes.remove(hitNode);
                    edges.removeIf(ed -> ed.from.id == hitNode.id || ed.to.id == hitNode.id);
                    if (selectedNode == hitNode) selectedNode = null;
                    if (srcNode  == hitNode) srcNode  = null;
                    if (destNode == hitNode) destNode = null;
                    redraw();
                } else {
                    GraphEdge nearest = nearestEdge(mx, my);
                    if (nearest != null) { edges.remove(nearest); redraw(); }
                }
            }
        });
    }

    private void editEdgeWeight(GraphEdge edge) {
        TextInputDialog dlg = new TextInputDialog(String.valueOf(edge.weight));
        dlg.setTitle("Edge Weight");
        dlg.setHeaderText("Edge: " + edge.from.label + " \u2192 " + edge.to.label);
        dlg.setContentText("New weight (positive integer):");
        dlg.getDialogPane().setStyle("-fx-background-color: #313244;");
        dlg.showAndWait().ifPresent(s -> {
            try { edge.weight = Math.max(1, Integer.parseInt(s.trim())); redraw(); }
            catch (NumberFormatException ignored) {}
        });
    }

    // ── Drawing ───────────────────────────────────────────────────────────────

    private void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth(), h = canvas.getHeight();

        gc.setFill(Color.web("#1e1e2e"));
        gc.fillRect(0, 0, w, h);

        gc.setFill(Color.web("#45475a"));
        gc.setFont(Font.font("Arial", 11));
        gc.fillText("Left-click: add node  |  Click 2 nodes: add edge  |  Click edge: edit weight  |  Right-click: delete", 10, 18);

        for (GraphEdge edge : edges) drawEdge(gc, edge);
        for (GraphNode node : nodes) drawNode(gc, node);

        if (selectedNode != null) {
            gc.setStroke(Color.web("#cba6f7")); gc.setLineWidth(3);
            gc.strokeOval(selectedNode.x - NODE_RADIUS - 5, selectedNode.y - NODE_RADIUS - 5,
                (NODE_RADIUS + 5) * 2, (NODE_RADIUS + 5) * 2);
        }
    }

    private void drawNode(GraphicsContext gc, GraphNode node) {
        Color fill;
        if (!pathIds.isEmpty() && pathIds.contains(node.id)) {
            fill = Color.web("#a6e3a1");        // green — on final path
        } else if (activeNode != null && node.id == activeNode.id) {
            fill = Color.web("#f9e2af");        // yellow — active
        } else if (examineNode != null && node.id == examineNode.id) {
            fill = Color.web("#fab387");        // peach — examining
        } else if (resultIds.contains(node.id)) {
            fill = Color.web("#a6e3a1");        // green — topo result
        } else if (visitedIds.contains(node.id)) {
            fill = Color.web("#89dceb");        // teal — visited
        } else {
            fill = Color.web("#cdd6f4");        // white-blue — unvisited
        }

        gc.setFill(fill);
        gc.fillOval(node.x - NODE_RADIUS, node.y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

        // Border ring — gold for src, red for dest, else default
        if (srcNode != null && node.id == srcNode.id) {
            gc.setStroke(Color.web("#f9e2af")); gc.setLineWidth(3.5);
        } else if (destNode != null && node.id == destNode.id) {
            gc.setStroke(Color.web("#f38ba8")); gc.setLineWidth(3.5);
        } else {
            gc.setStroke(Color.web("#313244")); gc.setLineWidth(2.5);
        }
        gc.strokeOval(node.x - NODE_RADIUS, node.y - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);

        // Node letter
        gc.setFill(Color.web("#1e1e2e"));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        gc.fillText(node.label, node.x - 5, node.y + 6);

        // Dijkstra distance above node
        if (distMap != null) {
            Integer d = distMap.get(node.id);
            String ds = (d == null || d == Integer.MAX_VALUE) ? "\u221E" : String.valueOf(d);
            gc.setFill(Color.web("#f9e2af"));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            gc.fillText("d=" + ds, node.x - 10, node.y - NODE_RADIUS - 5);
        }

        // Topo in-degree above node
        if (inDegreeMap != null) {
            Integer deg = inDegreeMap.get(node.id);
            if (deg != null) {
                gc.setFill(Color.web("#cba6f7"));
                gc.setFont(Font.font("Arial", 11));
                gc.fillText("in:" + deg, node.x - 12, node.y - NODE_RADIUS - 5);
            }
        }

        // SRC / DEST tag below node
        if (srcNode != null && node.id == srcNode.id) {
            gc.setFill(Color.web("#f9e2af"));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            gc.fillText("SRC", node.x - 10, node.y + NODE_RADIUS + 13);
        }
        if (destNode != null && node.id == destNode.id) {
            gc.setFill(Color.web("#f38ba8"));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            gc.fillText("DEST", node.x - 12, node.y + NODE_RADIUS + 13);
        }
    }

    private void drawEdge(GraphicsContext gc, GraphEdge edge) {
        double x1 = edge.from.x, y1 = edge.from.y, x2 = edge.to.x, y2 = edge.to.y;
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double sx = x1 + Math.cos(angle) * NODE_RADIUS, sy = y1 + Math.sin(angle) * NODE_RADIUS;
        double ex = x2 - Math.cos(angle) * NODE_RADIUS, ey = y2 - Math.sin(angle) * NODE_RADIUS;

        String fwd = edge.from.id + "-" + edge.to.id;
        String bwd = edge.to.id  + "-" + edge.from.id;
        boolean isPath   = pathEdgeKeys.contains(fwd) || pathEdgeKeys.contains(bwd);
        boolean isActive = activeNode != null && examineNode != null &&
            ((edge.from.id == activeNode.id && edge.to.id == examineNode.id) ||
             (edge.from.id == examineNode.id && edge.to.id == activeNode.id));

        if (isPath)        { gc.setStroke(Color.web("#a6e3a1")); gc.setLineWidth(3.5); }
        else if (isActive) { gc.setStroke(Color.web("#f9e2af")); gc.setLineWidth(3);   }
        else               { gc.setStroke(Color.web("#585b70")); gc.setLineWidth(2);   }

        gc.strokeLine(sx, sy, ex, ey);
        if (isDirected) drawArrow(gc, sx, sy, ex, ey, isPath, isActive);

        // Weight badge
        double midX = (sx + ex) / 2, midY = (sy + ey) / 2;
        String ws = String.valueOf(edge.weight);
        gc.setFill(Color.web("#313244"));
        gc.fillRoundRect(midX - 10, midY - 14, 20, 16, 4, 4);
        gc.setFill(isPath ? Color.web("#a6e3a1") : isActive ? Color.web("#f9e2af") : Color.web("#cdd6f4"));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        gc.fillText(ws, midX - (ws.length() > 1 ? 7 : 4), midY - 2);
    }

    private void drawArrow(GraphicsContext gc, double sx, double sy,
                           double ex, double ey, boolean isPath, boolean isActive) {
        double angle = Math.atan2(ey - sy, ex - sx), len = 12, spread = Math.toRadians(25);
        gc.setStroke(isPath ? Color.web("#a6e3a1") : isActive ? Color.web("#f9e2af") : Color.web("#585b70"));
        gc.strokeLine(ex, ey, ex - len * Math.cos(angle - spread), ey - len * Math.sin(angle - spread));
        gc.strokeLine(ex, ey, ex - len * Math.cos(angle + spread), ey - len * Math.sin(angle + spread));
    }

    private GraphNode nodeAt(double x, double y) {
        for (GraphNode n : nodes)
            if (Math.hypot(n.x - x, n.y - y) <= NODE_RADIUS) return n;
        return null;
    }

    private GraphEdge nearestEdge(double x, double y) {
        GraphEdge nearest = null; double minDist = 10;
        for (GraphEdge e : edges) {
            double d = pointToSegmentDist(x, y, e.from.x, e.from.y, e.to.x, e.to.y);
            if (d < minDist) { minDist = d; nearest = e; }
        }
        return nearest;
    }

    private double pointToSegmentDist(double px, double py,
                                      double ax, double ay, double bx, double by) {
        double dx = bx - ax, dy = by - ay;
        if (dx == 0 && dy == 0) return Math.hypot(px - ax, py - ay);
        double t = Math.max(0, Math.min(1, ((px-ax)*dx + (py-ay)*dy) / (dx*dx + dy*dy)));
        return Math.hypot(px - (ax + t*dx), py - (ay + t*dy));
    }
}