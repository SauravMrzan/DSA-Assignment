// This program calculates the minimum number of road segments needed to deliver packages
//  across a network of roads. It uses a breadth-first search (BFS) approach to traverse the roads, 
//  starting from the source node. For each node, the program checks its neighbors and if a package 
//  is present at that node, it adds 2 to the road count to account for the return trip. The program
//   ensures that each road is traversed only once, and it efficiently computes the minimum number of
//    roads required to deliver the packages while considering both the delivery and return trips for 
//    each node that has a package.




import java.util.*;

public class Q4BPackageDelivery {

    // This method calculates the total number of road segments needed 
    // to deliver packages, considering that each visited package node 
    // requires a round-trip traversal.
    public static int minRoadsToTraverse(int[] packages, int[][] roads) {
        int n = packages.length;
        List<List<Integer>> graph = new ArrayList<>();

        // Initialize the adjacency list for all nodes
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // Build the undirected graph by connecting nodes based on the road data
        for (int[] road : roads) {
            graph.get(road[0]).add(road[1]);
            graph.get(road[1]).add(road[0]);
        }

        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        int roadsCount = 0;

        // Start the traversal from node 0
        queue.offer(0);
        visited[0] = true;

        // Use a breadth-first search to explore the graph
        while (!queue.isEmpty()) {
            int currentNode = queue.poll();

            // Process each neighbor of the current node
            for (int neighbor : graph.get(currentNode)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    // If this neighbor holds a package, add two roads (for the trip and return)
                    if (packages[neighbor] == 1) {
                        roadsCount += 2;
                    }
                    queue.offer(neighbor);
                }
            }
        }

        return roadsCount;
    }

    public static void main(String[] args) {
        // Sample input: packages and corresponding road connections
        int[] packages1 = { 1, 0, 0, 0, 0, 1 };
        int[][] roads1 = { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 } };

        // Output the computed minimal road traversals required
        System.out.println("Output: " + minRoadsToTraverse(packages1, roads1)); // Expected output: 2
    }
}
