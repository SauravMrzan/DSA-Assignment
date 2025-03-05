// Question 5
/*
 * Network Optimizer Application
 * ---------------------------------
 * This Java Swing-based GUI application allows users to design a network topology.
 * Users can:
 *add nodes (red = server, blue = client).
 * 
 * A basic adjacency matrix for graph representation.
 * Dijkstra’s algorithm to find the shortest path.
 *
 * 
 * Developed for a custom network design tool, avoiding pre-built graph libraries.
 */

 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.*;
 import java.util.*;
 
 // Class representing a network node (server/client)
 class Node {
     int id, x, y;      // Node ID, coordinates (x, y)
     boolean isServer;  // Flag: true for server, false for client
 
     Node(int id, int x, int y, boolean isServer) {
         this.id = id;
         this.x = x;
         this.y = y;
         this.isServer = isServer;
     }
 }
 
 // Class representing a network connection (edge)
 class Edge {
     int from, to, cost, bandwidth;
 
     Edge(int from, int to, int cost, int bandwidth) {
         this.from = from;
         this.to = to;
         this.cost = cost;
         this.bandwidth = bandwidth;
     }
 }
 
 // Main GUI panel for network visualization and interaction
 public class Q5NetworkOptimizer extends JPanel {
     private final java.util.List<Node> nodes = new ArrayList<>();  // List of nodes
     private final java.util.List<Edge> edges = new ArrayList<>();  // List of edges
     private int nodeCount = 0;  // Track number of nodes
     private int selectedNode1 = -1, selectedNode2 = -1;  // Track nodes for shortest path
     private java.util.List<Integer> shortestPath = new ArrayList<>();  // Store shortest path
 
     public Q5NetworkOptimizer() {
         // Mouse listener for adding nodes and selecting for shortest path
         addMouseListener(new MouseAdapter() {
             public void mousePressed(MouseEvent e) {
                 if (SwingUtilities.isRightMouseButton(e)) {
                     // Right-click: Select nodes for shortest path calculation
                     selectNode(e.getX(), e.getY());
                 } else {
                     // Left-click: Add a new node
                     addNode(e.getX(), e.getY());
                 }
             }
         });
     }
 
     // Add a new node at clicked position
     private void addNode(int x, int y) {
         nodes.add(new Node(nodeCount++, x, y, nodeCount % 2 == 0));  // Alternate between server/client
         repaint();  // Refresh UI
     }
 
     // Add an edge (connection) between two nodes
     private void addEdge(int from, int to, int cost, int bandwidth) {
         edges.add(new Edge(from, to, cost, bandwidth));
         repaint();  // Refresh UI
     }
 
     // Select a node for shortest path calculation
     private void selectNode(int x, int y) {
         for (Node node : nodes) {
             if (Math.abs(node.x - x) < 15 && Math.abs(node.y - y) < 15) {
                 if (selectedNode1 == -1) {
                     selectedNode1 = node.id;  // First node selected
                 } else {
                     selectedNode2 = node.id;  // Second node selected
                     computeShortestPath();  // Compute shortest path
                     selectedNode1 = -1;  // Reset selection
                     selectedNode2 = -1;
                 }
                 break;
             }
         }
         repaint();  // Refresh UI
     }
 
     // Compute the shortest path between two selected nodes using Dijkstra’s algorithm
     private void computeShortestPath() {
         if (selectedNode1 == -1 || selectedNode2 == -1) return;
 
         int n = nodes.size();
         int[][] adjMatrix = new int[n][n];
         for (int[] row : adjMatrix) Arrays.fill(row, Integer.MAX_VALUE);  // Initialize adjacency matrix
 
         // Populate adjacency matrix with edge costs
         for (Edge edge : edges) {
             adjMatrix[edge.from][edge.to] = edge.cost;
             adjMatrix[edge.to][edge.from] = edge.cost;
         }
 
         shortestPath = dijkstra(adjMatrix, selectedNode1, selectedNode2);  // Find shortest path
         repaint();  // Refresh UI
     }
 
     // Dijkstra’s algorithm implementation to find the shortest path
     private java.util.List<Integer> dijkstra(int[][] graph, int start, int end) {
         int n = graph.length;
         int[] dist = new int[n];  // Stores shortest distances
         int[] prev = new int[n];  // Stores previous nodes for path reconstruction
         boolean[] visited = new boolean[n];  // Track visited nodes
 
         Arrays.fill(dist, Integer.MAX_VALUE);
         Arrays.fill(prev, -1);
         dist[start] = 0;
 
         // Min-priority queue for selecting the node with the shortest known distance
         PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(i -> dist[i]));
         pq.add(start);
 
         while (!pq.isEmpty()) {
             int u = pq.poll();  // Extract node with minimum distance
             if (visited[u]) continue;
             visited[u] = true;
 
             // Relax edges
             for (int v = 0; v < n; v++) {
                 if (graph[u][v] != Integer.MAX_VALUE && !visited[v]) {
                     int newDist = dist[u] + graph[u][v];
                     if (newDist < dist[v]) {
                         dist[v] = newDist;
                         prev[v] = u;
                         pq.add(v);
                     }
                 }
             }
         }
 
         // Reconstruct shortest path
         java.util.List<Integer> path = new ArrayList<>();
         for (int at = end; at != -1; at = prev[at]) {
             path.add(at);
         }
         Collections.reverse(path);
         return path;
     }
 
     // Paint method to draw network nodes, edges, and highlight the shortest path
     protected void paintComponent(Graphics g) {
         super.paintComponent(g);
 
         // Draw edges
         for (Edge edge : edges) {
             Node n1 = nodes.get(edge.from);
             Node n2 = nodes.get(edge.to);
             boolean isShortestPath = false;
 
             // Highlight shortest path in green
             for (int i = 0; i < shortestPath.size() - 1; i++) {
                 if ((shortestPath.get(i) == edge.from && shortestPath.get(i + 1) == edge.to) ||
                     (shortestPath.get(i) == edge.to && shortestPath.get(i + 1) == edge.from)) {
                     isShortestPath = true;
                     break;
                 }
             }
 
             g.setColor(isShortestPath ? Color.GREEN : Color.BLACK);
             g.drawLine(n1.x, n1.y, n2.x, n2.y);
             g.setColor(Color.BLACK);
             g.drawString("NPR" + edge.cost + ", BW:" + edge.bandwidth, (n1.x + n2.x) / 2, (n1.y + n2.y) / 2);
         }
 
         // Draw nodes
         for (Node node : nodes) {
             g.setColor(node.isServer ? Color.RED : Color.BLUE);
             g.fillOval(node.x - 10, node.y - 10, 20, 20);
             g.setColor(Color.BLACK);
             g.drawString("N" + node.id, node.x - 5, node.y - 15);
         }
     }
 
     // Main method to initialize the GUI
     public static void main(String[] args) {
         JFrame frame = new JFrame("Network Optimizer");
         Q5NetworkOptimizer panel = new Q5NetworkOptimizer();
 
         JButton addEdgeButton = new JButton("Add Edge");
         JTextField fromField = new JTextField(2);
         JTextField toField = new JTextField(2);
         JTextField costField = new JTextField(3);
         JTextField bwField = new JTextField(3);
 
         JPanel controlPanel = new JPanel();
         controlPanel.add(new JLabel("From:"));
         controlPanel.add(fromField);
         controlPanel.add(new JLabel("To:"));
         controlPanel.add(toField);
         controlPanel.add(new JLabel("Cost:"));
         controlPanel.add(costField);
         controlPanel.add(new JLabel("BW:"));
         controlPanel.add(bwField);
         controlPanel.add(addEdgeButton);
 
         addEdgeButton.addActionListener(e -> {
             try {
                 int from = Integer.parseInt(fromField.getText());
                 int to = Integer.parseInt(toField.getText());
                 int cost = Integer.parseInt(costField.getText());
                 int bw = Integer.parseInt(bwField.getText());
                 panel.addEdge(from, to, cost, bw);
             } catch (NumberFormatException ignored) {}
         });
 
         frame.setLayout(new BorderLayout());
         frame.add(panel, BorderLayout.CENTER);
         frame.add(controlPanel, BorderLayout.SOUTH);
         frame.setSize(700, 700);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
     }
 }