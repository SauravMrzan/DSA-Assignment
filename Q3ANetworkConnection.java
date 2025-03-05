// The code employs Kruskal's algorithm together with a Union-Find data structure
// to compute the minimum cost required to connect devices by accounting for both
// module installation fees and direct connection expenses, ensuring all devices
// are linked at the lowest possible cost.
import java.util.*; // Import all required utilities from the java.util package

// Class implementing the Union-Find (Disjoint Set) data structure for managing connected components
class UnionFind {
    private int[] parent, rank; // Arrays to store each node's parent and the depth (rank) for optimization

    // Constructor to initialize the Union-Find structure for n elements
    public UnionFind(int n) {
        parent = new int[n]; // Initialize the parent array
        rank = new int[n]; // Initialize the rank array
        for (int i = 0; i < n; i++) {
            parent[i] = i; // Each element starts as its own parent
            rank[i] = 0; // Initial rank for each element is set to zero
        }
    }

    // Method to find the representative (root) of the set that element x belongs to, with path compression
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Recursively update parent pointers to the root
        }
        return parent[x]; // Return the root of x
    }

    // Method to merge the sets containing x and y; returns true if a merge occurs, false if they are already connected
    public boolean union(int x, int y) {
        int rootX = find(x); // Find the root of element x
        int rootY = find(y); // Find the root of element y

        // If both elements are already in the same set, no merge is necessary
        if (rootX == rootY)
            return false;

        // Attach the tree with lower rank to the tree with higher rank for balance
        if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX; // Attach y's tree under x's root
        } else if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY; // Attach x's tree under y's root
        } else {
            parent[rootY] = rootX; // If both ranks are equal, attach y under x
            rank[rootX]++; // Increase the rank of the new root
        }
        return true; // The union was successful
    }
}

public class Q3ANetworkConnection {

    // Function to determine the minimum cost to connect all devices, accounting for both module installation and direct connections
    public static int minCostToConnectDevices(int n, int[] modules, int[][] connections) {
        List<int[]> edges = new ArrayList<>(); // List to collect all potential connection edges

        // Step 1: Represent module installation costs as virtual edges from node 0 to each device (node i+1)
        for (int i = 0; i < n; i++) {
            edges.add(new int[] { modules[i], 0, i + 1 });
        }

        // Step 2: Include all provided direct connection edges between devices
        for (int[] conn : connections) {
            edges.add(new int[] { conn[2], conn[0], conn[1] });
        }

        // Step 3: Sort all edges in ascending order based on cost for the execution of Kruskal's algorithm
        edges.sort(Comparator.comparingInt(a -> a[0]));

        // Step 4: Use Kruskal's algorithm with Union-Find to construct the Minimum Spanning Tree (MST)
        UnionFind uf = new UnionFind(n + 1); // Initialize Union-Find for n devices plus the virtual node 0
        int totalCost = 0, edgesUsed = 0; // Variables to accumulate the total cost and count the edges added to the MST

        // Process each edge in order of increasing cost and merge sets if they are not already connected
        for (int[] edge : edges) {
            int cost = edge[0], u = edge[1], v = edge[2];
            if (uf.union(u, v)) { // Merge the sets if u and v are not already connected
                totalCost += cost; // Add the cost of this edge to the total cost
                edgesUsed++; // Increment the count of edges added
                if (edgesUsed == n) // Once n edges are added, all devices are connected
                    break;
            }
        }

        return totalCost; // Return the minimum cost to connect all devices
    }

    public static void main(String[] args) {
        int n = 3; // Total number of devices
        int[] modules = { 1, 2, 2 }; // Installation costs for each device's module
        int[][] connections = { { 1, 2, 1 }, { 2, 3, 1 } }; // Costs for direct connections between devices

        // Compute and display the minimum cost required to connect all devices
        System.out.println(minCostToConnectDevices(n, modules, connections)); // Expected output: 3
    }
}

// Expected Output:
// 3
