// Question 2 B
import java.util.*;

/**
 * This program finds the lexicographically smallest pair of points
 * that are closest to each other in a 2D plane based on Manhattan distance.
 */
public class Q2BClosestPoint {
    
    // Function to find the closest lexicographical pair
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int minDistance = Integer.MAX_VALUE;
        int[] result = new int[2];
        
        // Iterate over all pairs of points
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calculate Manhattan distance between points (i, j)
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);
                
                // Update if a smaller distance is found or if lexicographically smaller
                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance;
                    result[0] = i;
                    result[1] = j;
                }
            }
        }
        
        return result;
    }
    
    // Main function to test the implementation
    public static void main(String[] args) {
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};
        
        int[] closestPair = findClosestPair(x_coords, y_coords);
        System.out.println(Arrays.toString(closestPair)); // Expected Output: [0, 3]
    }
}