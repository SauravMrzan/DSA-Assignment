// This Java-based application uses Swing for its GUI to create network topology designs.  
// Users can visualize network structures by interacting with the interface, which allows them to add nodes as devices and create edges to establish connections between the nodes.  
// The program includes an MST calculation using Kruskal's algorithm for network optimization and displays the Minimum Spanning Tree.  
// It also supports shortest-path queries via Dijkstra’s algorithm to provide optimized network routes based on the connection layout.  
// The system allows users to add, modify, or delete nodes and edges, making it ideal for network topology simulation and optimization tasks.  
// The application features a user-friendly interface, suitable for individuals without prior experience in network design.

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

// Main class for the Network Topology GUI
public class Q5NetworkOptimizer extends JFrame { // Changed class name to Q5NetworkOptimizer
    private Graph graph; // Instance of the Graph class to represent the network
    private GraphPanel graphPanel; // Panel to draw the graph

    // Constructor for the Q5NetworkOptimizerNetworkOptimizerNetworkOptimizer
    public Q5NetworkOptimizer() { // Changed constructor name to Q5NetworkOptimizer
        graph = new Graph(); // Initialize the graph
        graphPanel = new GraphPanel(graph); // Create a panel to display the graph

        // Set up the JFrame properties
        setTitle("Network Topology Designer"); // Set the title of the window
        setSize(800, 600); // Set the size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application on close
        setLayout(new BorderLayout()); // Set layout manager to BorderLayout

        add(graphPanel, BorderLayout.CENTER); // Add the graph panel to the center of the frame

        // Create a control panel for buttons
        JPanel controlPanel = new JPanel();
        JButton addNodeButton = new JButton("Add Node"); // Button to add a node
        JButton addEdgeButton = new JButton("Add Edge"); // Button to add an edge
        JButton optimizeButton = new JButton("Optimize Network"); // Button to optimize the network
        JButton calculatePathButton = new JButton("Calculate Path"); // Button to calculate the shortest path

        // Add buttons to the control panel
        controlPanel.add(addNodeButton);
        controlPanel.add(addEdgeButton);
        controlPanel.add(optimizeButton);
        controlPanel.add(calculatePathButton);
        add(controlPanel, BorderLayout.SOUTH); // Add control panel to the bottom of the frame

        // Action listener for adding a node
        addNodeButton.addActionListener(e -> {
            String nodeName = JOptionPane.showInputDialog("Enter node name:"); // Prompt for node name
            if (nodeName != null && !nodeName.trim().isEmpty()) { // Check if input is valid
                graph.addNode(nodeName); // Add node to the graph
                graphPanel.repaint(); // Repaint the graph panel to reflect changes
            } else {
                JOptionPane.showMessageDialog(null, "Node name cannot be empty."); // Show error if input is invalid
            }
        });

        // Action listener for adding an edge
        addEdgeButton.addActionListener(e -> {
            String fromNode = JOptionPane.showInputDialog("Enter from node:"); // Prompt for starting node
            String toNode = JOptionPane.showInputDialog("Enter to node:"); // Prompt for ending node
            String costStr = JOptionPane.showInputDialog("Enter cost:"); // Prompt for edge cost
            String bandwidthStr = JOptionPane.showInputDialog("Enter bandwidth:"); // Prompt for edge bandwidth

            // Check if all inputs are provided
            if (fromNode != null && toNode != null && costStr != null && bandwidthStr != null) {
                try {
                    int cost = Integer.parseInt(costStr); // Parse cost to integer
                    int bandwidth = Integer.parseInt(bandwidthStr); // Parse bandwidth to integer
                    if (cost < 0 || bandwidth < 0) { // Check for non-negative values
                        JOptionPane.showMessageDialog(null, "Cost and bandwidth must be non-negative."); // Show error
                    } else {
                        graph.addEdge(fromNode, toNode, cost, bandwidth); // Add edge to the graph
                        graphPanel.repaint(); // Repaint the graph panel
                    }
                } catch (NumberFormatException ex) { // Handle invalid number format
                    JOptionPane.showMessageDialog(null, "Invalid cost or bandwidth."); // Show error
                }
            } else {
                JOptionPane.showMessageDialog(null, "All fields must be filled."); // Show error if fields are empty
            }
        });

        // Action listener for optimizing the network
        optimizeButton.addActionListener(e -> {
            List<Edge> mstEdges = graph.getMinimumSpanningTree(); // Get minimum spanning tree edges
            StringBuilder result = new StringBuilder("Minimum Spanning Tree Edges:\n"); // Prepare result string
            for (Edge edge : mstEdges) { // Iterate through edges
                result.append(edge.getDestination().getName()).append(" (Cost: ").append(edge.getCost()).append(")\n"); // Append
                                                                                                                        // edge
                                                                                                                        // info
            }
            JOptionPane.showMessageDialog(null, result.toString()); // Show result in a dialog
        });

        // Action listener for calculating the shortest path
        calculatePathButton.addActionListener(e -> {
            String fromNode = JOptionPane.showInputDialog("Enter start node:"); // Prompt for start node
            String toNode = JOptionPane.showInputDialog("Enter end node:"); // Prompt for end node
            if (fromNode != null && toNode != null) { // Check if inputs are valid
                List<Node> path = graph.findShortestPath(fromNode, toNode); // Find shortest path
                if (path != null) { // Check if a path was found
                    StringBuilder result = new StringBuilder("Shortest Path:\n"); // Prepare result string
                    for (Node node : path) { // Iterate through nodes in the path
                        result.append(node.getName()).append(" -> "); // Append node names
                    }
                    result.setLength(result.length() - 4); // Remove last arrow
                    JOptionPane.showMessageDialog(null, result.toString()); // Show result in a dialog
                } else {
                    JOptionPane.showMessageDialog(null, "No path found."); // Show error if no path found
                }
            }
        });
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Q5NetworkOptimizer gui = new Q5NetworkOptimizer(); // Create GUI instance
            gui.setVisible(true); // Make the GUI visible
        });
    }
}

// Class representing the graph structure
class Graph {
    private final Map<String, Node> nodes = new HashMap<>(); // Map to store nodes by name

    // Method to add a node to the graph
    public void addNode(String name) {
        nodes.put(name, new Node(name)); // Create and add a new node
    }

    // Method to add an edge between two nodes
    public void addEdge(String from, String to, int cost, int bandwidth) {
        Node source = nodes.get(from); // Get source node
        Node destination = nodes.get(to); // Get destination node
        if (source != null && destination != null) { // Check if both nodes exist
            source.addEdge(new Edge(destination, cost, bandwidth)); // Add edge to the source node
        } else {
            JOptionPane.showMessageDialog(null, "One or both nodes do not exist."); // Show error if nodes do not exist
        }
    }

    // Method to get all nodes in the graph
    public Collection<Node> getNodes() {
        return nodes.values(); // Return collection of nodes
    }

    // Method to get the minimum spanning tree of the graph
    public List<Edge> getMinimumSpanningTree() {
        List<Edge> mstEdges = new ArrayList<>(); // List to store edges of the minimum spanning tree
        Set<Node> visited = new HashSet<>(); // Set to track visited nodes
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparingInt(Edge::getCost)); // Priority queue
                                                                                                     // for edges based
                                                                                                     // on cost

        if (!nodes.isEmpty()) { // Check if there are nodes in the graph
            Node startNode = nodes.values().iterator().next(); // Get an arbitrary start node
            visited.add(startNode); // Mark start node as visited
            edgeQueue.addAll(startNode.getEdges()); // Add edges of the start node to the queue

            // Loop until there are no more edges to process
            while (!edgeQueue.isEmpty()) {
                Edge edge = edgeQueue.poll(); // Get the edge with the lowest cost
                Node destination = edge.getDestination(); // Get the destination node of the edge
                if (!visited.contains(destination)) { // Check if the destination node has not been visited
                    visited.add(destination); // Mark destination node as visited
                    mstEdges.add(edge); // Add edge to the minimum spanning tree
                    edgeQueue.addAll(destination.getEdges()); // Add edges of the newly visited node to the queue
                }
            }
        }
        return mstEdges; // Return the edges of the minimum spanning tree
    }

    // Method to find the shortest path between two nodes
    public List<Node> findShortestPath(String startName, String endName) {
        Node startNode = nodes.get(startName); // Get the start node
        Node endNode = nodes.get(endName); // Get the end node
        if (startNode == null || endNode == null)
            return null; // Return null if either node does not exist

        Map<Node, Integer> distances = new HashMap<>(); // Map to store distances from the start node
        Map<Node, Node> previousNodes = new HashMap<>(); // Map to store previous nodes in the path
        PriorityQueue<Node> nodeQueue = new PriorityQueue<>(Comparator.comparingInt(distances::get)); // Priority queue
                                                                                                      // for nodes based
                                                                                                      // on distance

        // Initialize distances and previous nodes
        for (Node node : nodes.values()) {
            distances.put(node, Integer.MAX_VALUE); // Set initial distance to infinity
            previousNodes.put(node, null); // Set previous node to null
        }
        distances.put(startNode, 0); // Set distance to start node as 0
        nodeQueue.add(startNode); // Add start node to the queue

        // Loop until there are no more nodes to process
        while (!nodeQueue.isEmpty()) {
            Node currentNode = nodeQueue.poll(); // Get the node with the smallest distance
            if (currentNode.equals(endNode))
                break; // Stop if we reached the end node

            // Iterate through edges of the current node
            for (Edge edge : currentNode.getEdges()) {
                Node neighbor = edge.getDestination(); // Get the neighboring node
                int newDist = distances.get(currentNode) + edge.getCost(); // Calculate new distance
                if (newDist < distances.get(neighbor)) { // Check if the new distance is shorter
                    distances.put(neighbor, newDist); // Update distance
                    previousNodes.put(neighbor, currentNode); // Update previous node
                    nodeQueue.add(neighbor); // Add neighbor to the queue
                }
            }
        }

        // Build the path from end node to start node
        List<Node> path = new ArrayList<>();
        for (Node at = endNode; at != null; at = previousNodes.get(at)) {
            path.add(at); // Add node to the path
        }
        Collections.reverse(path); // Reverse the path to get it from start to end
        return path.size() > 1 ? path : null; // Return the path if it contains more than one node, otherwise return
                                              // null
    }
}

// Class representing a node in the graph
class Node {
    private final String name; // Name of the node
    private final List<Edge> edges = new ArrayList<>(); // List of edges connected to the node

    // Constructor for the Node class
    Node(String name) {
        this.name = name; // Set the name of the node
    }

    // Method to add an edge to the node
    void addEdge(Edge edge) {
        edges.add(edge); // Add edge to the list
    }

    // Method to get the name of the node
    public String getName() {
        return name; // Return the name
    }

    // Method to get the edges connected to the node
    public List<Edge> getEdges() {
        return edges; // Return the list of edges
    }
}

// Class representing an edge in the graph
class Edge {
    private final Node destination; // Destination node of the edge
    private final int cost; // Cost of the edge
    private final int bandwidth; // Bandwidth of the edge

    // Constructor for the Edge class
    Edge(Node destination, int cost, int bandwidth) {
        this.destination = destination; // Set the destination node
        this.cost = cost; // Set the cost
        this.bandwidth = bandwidth; // Set the bandwidth
    }

    // Method to get the destination node of the edge
    public Node getDestination() {
        return destination; // Return the destination node
    }

    // Method to get the cost of the edge
    public int getCost() {
        return cost; // Return the cost
    }

    // Method to get the bandwidth of the edge
    public int getBandwidth() {
        return bandwidth; // Return the bandwidth
    }
}

// Class for the panel that draws the graph
class GraphPanel extends JPanel {
    private final Graph graph; // Instance of the Graph class

    // Constructor for the GraphPanel class
    public GraphPanel(Graph graph) {
        this.graph = graph; // Set the graph instance
        setBackground(Color.WHITE); // Set the background color to white
    }

    // Method to paint the component
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass method
        drawGraph(g); // Draw the graph
    }

    // Method to draw the graph
    private void drawGraph(Graphics g) {
        int nodeRadius = 20; // Radius of the nodes
        int xOffset = 50; // X offset for positioning nodes
        int yOffset = 50; // Y offset for positioning nodes
        int spacing = 100; // Spacing between nodes
        int index = 0; // Index for positioning nodes

        // Draw nodes
        for (Node node : graph.getNodes()) {
            int x = xOffset + (index % 5) * spacing; // Calculate X position
            int y = yOffset + (index / 5) * spacing; // Calculate Y position
            g.fillOval(x, y, nodeRadius, nodeRadius); // Draw the node as a circle
            g.drawString(node.getName(), x + 5, y + 15); // Draw the node name
            index++; // Increment index
        }

        // Draw edges
        for (Node node : graph.getNodes()) {
            for (Edge edge : node.getEdges()) {
                // Calculate positions for the edge
                int fromX = xOffset + (Arrays.asList(graph.getNodes().toArray()).indexOf(node) % 5) * spacing
                        + nodeRadius / 2;
                int fromY = yOffset + (Arrays.asList(graph.getNodes().toArray()).indexOf(node) / 5) * spacing
                        + nodeRadius / 2;
                int toX = xOffset
                        + (Arrays.asList(graph.getNodes().toArray()).indexOf(edge.getDestination()) % 5) * spacing
                        + nodeRadius / 2;
                int toY = yOffset
                        + (Arrays.asList(graph.getNodes().toArray()).indexOf(edge.getDestination()) / 5) * spacing
                        + nodeRadius / 2;

                g.drawLine(fromX, fromY, toX, toY); // Draw the line representing the edge
                g.drawString("Cost: " + edge.getCost() + ", Bandwidth: " + edge.getBandwidth(), (fromX + toX) / 2,
                        (fromY + toY) / 2);
            }
        }
    }
}