// Question 4 B
import java.util.*;
  /*
     * This class provides a method to calculate the minimum number of roads needed 
     * to collect all packages located at various nodes in a graph, where the robot 
     * can collect packages from nodes that are within a distance of 2 roads. 
     * The robot must also return to its starting position.
     */
public class PackageDelivery {
    // Returns the minimum roads needed to collect all packages (distance <= 2) and return to start.
    public static int minRoads(int[] packages, int[][] roads) {
        int n = packages.length; // Number of nodes in the graph
        
        // Build adjacency list to represent the graph
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>()); // Initialize each node's adjacency list
        }
        for (int[] r : roads) {
            graph.get(r[0]).add(r[1]); // Add road from node r[0] to r[1]
            graph.get(r[1]).add(r[0]); // Add road from node r[1] to r[0] (undirected graph)
        }
        
        // 1) Identify package locations
        List<Integer> packageLoc = new ArrayList<>(); // List to store nodes with packages
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) {
                packageLoc.add(i); // Add node index to packageLoc if it has a package
            }
        }
        int M = packageLoc.size();  // Number of packages
        if (M == 0) {
            return 0; // If there are no packages, no roads need to be traversed
        }
        
        // 2) Precompute all-pairs shortest paths (BFS from each node)
        int[][] dist = new int[n][n]; // Distance matrix
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], -1); // Initialize distances to -1 (unreachable)
            Queue<Integer> queue = new LinkedList<>();
            dist[i][i] = 0; // Distance to itself is 0
            queue.offer(i); // Start BFS from node i
            while (!queue.isEmpty()) {
                int curr = queue.poll(); // Get the current node
                for (int neigh : graph.get(curr)) {
                    if (dist[i][neigh] == -1) { // If neighbor hasn't been visited
                        dist[i][neigh] = dist[i][curr] + 1; // Update distance
                        queue.offer(neigh); // Add neighbor to the queue
                    }
                }
            }
        }
        
        // 3) coverage[u] = bitmask of packages that can be collected by standing at node u
        int[] coverage = new int[n]; // Array to store coverage bitmasks
        for (int u = 0; u < n; u++) {
            int mask = 0; // Initialize mask for the current node
            for (int p = 0; p < M; p++) {
                int pkgNode = packageLoc.get(p); // Get the package node
                // If distance(u, pkgNode) <= 2, update the mask to include this package
                if (dist[u][pkgNode] != -1 && dist[u][pkgNode] <= 2) {
                    mask |= (1 << p); // Update the mask
                }
            }
            coverage[u] = mask; // Store the coverage for node u
        }
        
        // Multi-start BFS: try each node as start, keep track of best answer
        int bestAnswer = Integer.MAX_VALUE; // Initialize best answer to infinity
        
        for (int start = 0; start < n; start++) {
            // BFS state: (currentNode, bitmaskOfCollectedPackages)
            int[][] distState = new int[n][1 << M]; // Distance state for each node and package mask
            for (int i = 0; i < n; i++) {
                Arrays.fill(distState[i], Integer.MAX_VALUE); // Initialize to infinity
            }
            
            // Initial state: standing at 'start', collecting coverage[start]
            int initMask = coverage[start]; // Get initial coverage mask
            distState[start][initMask] = 0; // No roads used to start at 'start'
            
            Queue<int[]> queue = new LinkedList<>(); // Queue for BFS
            queue.offer(new int[]{start, initMask}); // Add initial state to the queue
            
            while (!queue.isEmpty()) {
                int[] cur = queue.poll(); // Get the current state
                int node = cur[0]; // Current node
                int mask = cur[1]; // Current package mask
                int roadsUsed = distState[node][mask]; // Roads used to reach this state
                
                // Check if all packages have been collected
                if (mask == (1 << M) - 1) {
                    // If all packages are collected, add cost to return to 'start'
                    if (dist[node][start] != -1) {
                        bestAnswer = Math.min(bestAnswer, roadsUsed + dist[node][start]); // Update best answer
                    }
                }
                
                // Move to neighbors
                for (int neigh : graph.get(node)) {
                    int newMask = mask | coverage[neigh]; // Update mask with coverage from neighbor
                    int newCost = roadsUsed + 1; // Increment roads used
                    // If this new state is better, update and add to the queue
                    if (newCost < distState[neigh][newMask]) {
                        distState[neigh][newMask] = newCost; // Update distance state
                        queue.offer(new int[]{neigh, newMask}); // Add new state to the queue
                    }
                }
            }
        }
        
        // Return the best answer found, or 0 if no valid path was found
        return (bestAnswer == Integer.MAX_VALUE) ? 0 : bestAnswer;
    }
    
    public static void main(String[] args) {
        // Test 1
        int[] packages1 = {1, 0, 0, 1, 0, 1}; // Package locations
        int[][] roads1 = {
            {0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5} // Road connections
        };
        System.out.println(minRoads(packages1, roads1)); // Expected output: 2
        
        // Test 2
        int[] packages2 = {0, 1, 0, 1, 1, 0, 0, 1}; // Package locations
        int[][] roads2 = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7} // Road connections
        };
        System.out.println(minRoads(packages2, roads2)); // Expected output: 2
    }
}