// Question 3 A
/**
 * This program calculates the minimum total cost required to connect all devices in a network.
 * 
 * Approach:
 *Each device can either install its own communication module (at a given cost) or 
 *connect to another device using a direct connection (at a given cost).
 *We use Kruskal's Minimum Spanning Tree (MST) algorithm to determine the optimal connections
 *between devices while minimizing costs.
 * To simulate module installation as an alternative to direct connections, a virtual node (node 0)
 * is added, and each device is given an edge connecting to it with the module cost.
 * The edges (both direct connections and module installations) are sorted by cost.
 * Using a Disjoint Set Union (DSU) data structure, we apply Kruskal's algorithm to connect 
 * devices at the lowest possible cost.
 * The process stops when all devices are connected, ensuring an optimal solution.
 * 
 * Time Complexity: O(E log E) where E is the number of edges (including module installations),
 * due to sorting and union-find operations.
 */

 import java.util.*;

 public class Q3ANetworkConnection {
     
     // Union-Find Data Structure for Kruskal's Algorithm
     static class DSU {
         int[] parent, rank;
         
         DSU(int n) {
             parent = new int[n];
             rank = new int[n];
             for (int i = 0; i < n; i++) {
                 parent[i] = i;
                 rank[i] = 1;
             }
         }
         
         // Find with path compression
         int find(int x) {
             if (parent[x] != x) {
                 parent[x] = find(parent[x]);
             }
             return parent[x];
         }
         
         // Union by rank
         boolean union(int x, int y) {
             int rootX = find(x);
             int rootY = find(y);
             
             if (rootX == rootY) return false; // Already connected
             
             if (rank[rootX] > rank[rootY]) {
                 parent[rootY] = rootX;
             } else if (rank[rootX] < rank[rootY]) {
                 parent[rootX] = rootY;
             } else {
                 parent[rootY] = rootX;
                 rank[rootX]++;
             }
             
             return true;
         }
     }
     
     public static int minNetworkCost(int n, int[] modules, int[][] connections) {
         List<int[]> edges = new ArrayList<>();
 
         // Add module installation costs as virtual edges (0 to each device)
         for (int i = 0; i < n; i++) {
             edges.add(new int[]{0, i + 1, modules[i]}); // Connecting virtual node 0
         }
         
         // Add given connections
         for (int[] conn : connections) {
             edges.add(new int[]{conn[0], conn[1], conn[2]});
         }
 
         // Sort edges based on cost (for Kruskal's Algorithm)
         edges.sort(Comparator.comparingInt(a -> a[2]));
 
         // Apply Kruskal’s Algorithm to find MST
         DSU dsu = new DSU(n + 1); // Extra node for virtual node 0
         int minCost = 0, edgesUsed = 0;
         
         for (int[] edge : edges) {
             int u = edge[0], v = edge[1], cost = edge[2];
             if (dsu.union(u, v)) {
                 minCost += cost;
                 edgesUsed++;
                 if (edgesUsed == n) break; // Stop when we've connected all devices
             }
         }
         
         return minCost;
     }
 
     // Test Cases
     public static void main(String[] args) {
         int n = 3;
         int[] modules = {1, 2, 2};
         int[][] connections = {{1, 2, 1}, {2, 3, 1}};
 
         System.out.println(minNetworkCost(n, modules, connections)); // Expected Output: 3
     }
 }