// The program identifies the k-th smallest product from two sorted arrays through 
// the implementation of a priority queue with min-heap functionality. 
// The program begins with the minimum product and proceeds to add new 
// index pairs through duplicate avoidance with a HashSet to grow its size.
//  The method continues its operations until it discovers and returns the 
//  k-th smallest product in an efficient manner.

import java.util.*; // Importing necessary libraries for PriorityQueue and HashSet

class Qn1b { // Class definition

    public static int kthSmallestProduct(int[] returns1, int[] returns2, int k) {
        // Creating a min-heap (priority queue) to store pairs of indices (i, j)
        // It sorts elements based on their multiplication results.
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
                Comparator.comparingInt(a -> returns1[a[0]] * returns2[a[1]]));

        // HashSet to track visited pairs and avoid duplicate entries in the heap
        Set<String> visited = new HashSet<>();

        // Start from the first pair (returns1[0] * returns2[0])
        minHeap.offer(new int[] { 0, 0 });
        visited.add("0,0"); // Mark this pair as visited

        int count = 0; // Counter to track how many elements have been processed

        while (!minHeap.isEmpty()) { // Continue until we find the k-th smallest product
            int[] pair = minHeap.poll(); // Remove the smallest product pair from the heap
            int i = pair[0], j = pair[1]; // Get indices from the pair
            int product = returns1[i] * returns2[j]; // Calculate the product
            count++; // Increase count as we found another smallest product

            if (count == k) // If we have found the k-th smallest product, return it
                return product;

            // Try adding the next element from returns1 while keeping returns2 the same
            if (i + 1 < returns1.length && visited.add((i + 1) + "," + j)) {
                minHeap.offer(new int[] { i + 1, j }); // Add the new pair to the heap
            }

            // Try adding the next element from returns2 while keeping returns1 the same
            if (j + 1 < returns2.length && visited.add(i + "," + (j + 1))) {
                minHeap.offer(new int[] { i, j + 1 }); // Add the new pair to the heap
            }
        }

        return -1; // This line should never be reached
    }

    public static void main(String[] args) {
        int[] returns1 = { -4, -2, 0, 3 }; // First sorted array
        int[] returns2 = { 2, 4 }; // Second sorted array
        int k = 6; // Find the 6th smallest product

        // Call the function and print the result
        System.out.println(kthSmallestProduct(returns1, returns2, k));
    }
}

// Output
// 0